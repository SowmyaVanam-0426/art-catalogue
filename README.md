# Art Catalogue – AWS DevOps Project

A cloud-native Art Catalogue application built to demonstrate end-to-end DevOps practices using **AWS, Terraform, Docker, Jenkins, Kubernetes and CI/CD automation**.

The project focuses on infrastructure provisioning, containerization, automated builds, security scanning, container registry management and Kubernetes deployments.

## Architecture

```text
                    Developer
                        |
                        v
                     GitHub
                        |
                        v
              Jenkins (Local Ubuntu)
                        |
             +----------+----------+
             |          |          |
             v          v          v
           Maven       npm       Trivy
           Tests      Build    Security Scan
             |          |          |
             +----------+----------+
                        |
                        v
                  Docker Build
                        |
                        v
                  Amazon ECR
                        |
                        v
                  Amazon EKS
                        |
              +---------+---------+
              |                   |
          Frontend             Backend
          React/Vite         Spring Boot
                                  |
                                  v
                            Amazon RDS
                              PostgreSQL
```

AWS infrastructure is provisioned and managed using Terraform.

## Technology Stack

| Category | Technologies |
|---|---|
| Frontend | React, Vite, JavaScript |
| Backend | Java 21, Spring Boot |
| Database | PostgreSQL |
| Infrastructure as Code | Terraform |
| Cloud Platform | AWS |
| Containerization | Docker |
| Container Registry | Amazon ECR |
| CI/CD | Jenkins |
| Container Orchestration | Amazon EKS, Kubernetes |
| Security Scanning | Trivy |
| Deployment Configuration | Kubernetes YAML |
| Source Control | Git, GitHub |

## AWS Infrastructure

The infrastructure is provisioned using Terraform in the `ap-southeast-2` region.

Key AWS resources:

- **Amazon VPC:** Isolated network environment.
- **Public and private subnets:** Network separation across availability zones.
- **Internet Gateway:** Internet connectivity for public subnets.
- **NAT Gateway:** Outbound internet access for private application subnets.
- **Amazon EKS:** Managed Kubernetes control plane.
- **EKS Managed Node Group:** EC2 worker nodes for application workloads.
- **Amazon ECR:** Separate repositories for frontend and backend images.
- **Amazon RDS:** Managed PostgreSQL database.
- **AWS Secrets Manager:** Secure storage of database credentials.
- **IAM:** Access management for AWS services and deployments.

## CI/CD Pipeline

Jenkins runs on a local Ubuntu machine and connects to AWS to execute the deployment pipeline.

The pipeline is designed to automate the following stages:

1. **Source Checkout**
   - Retrieves application source code from GitHub.
2. **Backend Testing**
   - Executes Maven tests for the Spring Boot application.
3. **Frontend Build**
   - Installs dependencies and builds the React application.
4. **Docker Image Build**
   - Builds separate frontend and backend container images.
5. **Security Scanning**
   - Uses Trivy to scan container images for HIGH and CRITICAL vulnerabilities.
6. **Amazon ECR Push**
   - Authenticates with ECR and pushes versioned images.
7. **Kubernetes Access**
   - Configures `kubectl` access to the EKS cluster.
8. **Database Configuration**
   - Retrieves database credentials from AWS Secrets Manager and creates the Kubernetes Secret.
9. **Application Deployment**
   - Applies Kubernetes Deployments, Services and Ingress configuration.
10. **Deployment Verification**
    - Checks Kubernetes rollout status and application resources.

## Kubernetes Deployment

The application is deployed in a dedicated Kubernetes namespace:

`art-catalogue`

Kubernetes resources include:

- Namespace
- ConfigMap
- Backend Deployment
- Backend ClusterIP Service
- Frontend Deployment
- Frontend ClusterIP Service
- Database credentials Secret
- Ingress configuration

The frontend and backend are deployed independently, allowing separate scaling and updates.

## Repository Structure

```text
art-catalogue/
│
├── backend/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── k8s/
│   ├── namespace.yml
│   ├── configmap.yml
│   ├── backend-deployment.yml
│   ├── backend-service.yml
│   ├── frontend-deployment.yml
│   ├── frontend-service.yml
│   └── ingress.yml
│
├── main.tf
├── backend.tf
├── Jenkinsfile
├── docker-compose.yml
├── .dockerignore
├── .gitignore
└── README.md
```

## Running Locally

### Prerequisites

- Docker
- Docker Compose

### Start the application

```bash
git clone https://github.com/SowmyaVanam-0426/art-catalogue.git

cd art-catalogue

sudo docker compose up -d --build
```

Check running containers:

```bash
sudo docker compose ps
```

Stop the application:

```bash
sudo docker compose down
```

## Deployment and Validation

Useful commands for verifying the EKS deployment:

```bash
aws eks update-kubeconfig \
  --region ap-southeast-2 \
  --name art-catalogue-dev
```

Check cluster nodes:

```bash
kubectl get nodes
```

Check application pods:

```bash
kubectl get pods -n art-catalogue
```

Check services:

```bash
kubectl get services -n art-catalogue
```

Check ingress:

```bash
kubectl get ingress -n art-catalogue
```

Check deployment rollout:

```bash
kubectl rollout status deployment/backend -n art-catalogue

kubectl rollout status deployment/frontend -n art-catalogue
```

## Security and Best Practices

- Terraform state and provider binaries are excluded from Git version control.
- Database credentials are stored in AWS Secrets Manager.
- Kubernetes Secrets are used for application database configuration.
- IAM roles are used for AWS service access instead of hardcoded access keys.
- Container images are scanned using Trivy.
- Application components are separated into independent containers.
- Kubernetes deployments define resource requests, limits and health probes.
- Infrastructure and application configuration are maintained separately.

## Project Objectives

This project demonstrates practical experience with:

- AWS infrastructure provisioning through Terraform.
- CI/CD pipeline implementation using Jenkins.
- Docker image creation and registry management.
- Kubernetes application deployment and troubleshooting.
- AWS EKS cluster administration.
- Integration between Jenkins, AWS ECR and EKS.
- Secrets management and IAM-based access control.
- Application health checks and deployment verification.

**Author:** Sowmya

**Project:** Art Catalogue – AWS DevOps Portfolio Project

**Repository:** https://github.com/SowmyaVanam-0426/art-catalogue