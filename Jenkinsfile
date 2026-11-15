
pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    environment {
        AWS_REGION = 'ap-southeast-2'
        EKS_CLUSTER = 'art-catalogue-dev'
        ECR_BACKEND = 'art-catalogue-backend'
        ECR_FRONTEND = 'art-catalogue-frontend'
        DB_SECRET = 'art-catalogue/dev/database'
        K8S_NAMESPACE = 'art-catalogue'
        KUBECONFIG = '/var/lib/jenkins/.kube/config'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh 'mvn -B clean test'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh '''
                        npm install --no-audit --no-fund
                        npm run build
                    '''
                }
            }
        }

        stage('Prepare AWS and Image Names') {
             steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                    script {
                        env.AWS_ACCOUNT_ID = sh(
                            script: 'aws sts get-caller-identity --query Account --output text',
                            returnStdout: true
                        ).trim()

                        env.BACKEND_IMAGE =
                            "${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com/${env.ECR_BACKEND}:${env.BUILD_NUMBER}"

                        env.FRONTEND_IMAGE =
                            "${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com/${env.ECR_FRONTEND}:${env.BUILD_NUMBER}"
                        }
                    }
                }
           }

        stage('Build Docker Images') {
            steps {
                sh '''
                    docker build -t "$BACKEND_IMAGE" ./backend
                    docker build -t "$FRONTEND_IMAGE" ./frontend
                '''
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh '''
                   trivy image \
                     --timeout 20m \
                      --severity HIGH,CRITICAL "$BACKEND_IMAGE"

                    trivy image \
                     --timeout 20m \
                      --severity HIGH,CRITICAL "$FRONTEND_IMAGE"
                '''
            }
        }

        stage('Push Images to ECR') {
           steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                sh '''
                    aws ecr get-login-password --region "$AWS_REGION" |
                    docker login \
                      --username AWS \
                      --password-stdin \
                      "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

                    docker push "$BACKEND_IMAGE"
                    docker push "$FRONTEND_IMAGE"
                '''
                }
           }
        }

        stage('Configure Kubernetes Access') {
            steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                    
                sh '''
                   unset KUBECONFIG
                   export KUBECONFIG="$HOME/.kube/config"

                aws eks update-kubeconfig \
                  --region "$AWS_REGION" \
                  --name "$EKS_CLUSTER"

                echo "Current Kubernetes context:"
                kubectl config current-context

                echo "Available contexts:"
                kubectl config get-contexts

                echo "EKS nodes:"
                kubectl get nodes
                '''
                }   
            }
        }

        stage('Prepare Database Secret') {
            steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                sh '''
                    set +x

                    SECRET_JSON=$(aws secretsmanager get-secret-value \
                      --region "$AWS_REGION" \
                      --secret-id "$DB_SECRET" \
                      --query SecretString \
                      --output text)

                    DB_USERNAME=$(printf '%s' "$SECRET_JSON" | jq -r '.username')
                    DB_PASSWORD=$(printf '%s' "$SECRET_JSON" | jq -r '.password')
                    DB_HOST=$(printf '%s' "$SECRET_JSON" | jq -r '.host')
                    DB_PORT=$(printf '%s' "$SECRET_JSON" | jq -r '.port')
                    DB_NAME=$(printf '%s' "$SECRET_JSON" | jq -r '.database')

                    kubectl create namespace "$K8S_NAMESPACE" \
                      --dry-run=client -o yaml | kubectl apply -f -

                    kubectl create secret generic db-credentials \
                      --namespace "$K8S_NAMESPACE" \
                      --from-literal=username="$DB_USERNAME" \
                      --from-literal=password="$DB_PASSWORD" \
                      --from-literal=host="$DB_HOST" \
                      --from-literal=port="$DB_PORT" \
                      --from-literal=database="$DB_NAME" \
                      --dry-run=client -o yaml |
                      kubectl apply -f -

                    unset SECRET_JSON DB_USERNAME DB_PASSWORD
                    unset DB_HOST DB_PORT DB_NAME
                '''
                }
            }
        }

        stage('Deploy to EKS') {
            steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                sh '''
                    kubectl apply -f k8s/namespace.yml
                    kubectl apply -f k8s/configmap.yml

                    export BACKEND_IMAGE
                    export FRONTEND_IMAGE

                    envsubst '${BACKEND_IMAGE}' \
                      < k8s/backend-deployment.yml |
                      kubectl apply -f -

                    kubectl apply -f k8s/backend-service.yml

                    envsubst '${FRONTEND_IMAGE}' \
                      < k8s/frontend-deployment.yml |
                      kubectl apply -f -

                    kubectl apply -f k8s/frontend-service.yml

                    kubectl apply -f k8s/ingress.yml
                '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws'
                ]]) {
                sh '''
                    kubectl rollout status deployment/backend \
                      -n "$K8S_NAMESPACE" --timeout=300s

                    kubectl rollout status deployment/frontend \
                      -n "$K8S_NAMESPACE" --timeout=300s

                    kubectl get pods -n "$K8S_NAMESPACE"
                    kubectl get services -n "$K8S_NAMESPACE"
                    kubectl get ingress -n "$K8S_NAMESPACE"
                '''
                }
            }
        }
    }

    post {
        success {
            echo 'Art Catalogue deployment completed successfully.'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }

        always {
            sh '''
                docker image prune -f || true
            '''
        }
    }
}