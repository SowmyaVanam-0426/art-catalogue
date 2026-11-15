package com.example.artcatalogue.artwork;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name="artworks")
public class Artwork {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=160) private String title;
  @Column(nullable=false,length=120) private String artist;
  @Column(nullable=false,length=80) private String category;
  private Integer yearCreated;
  @Column(length=120) private String medium;
  @Column(precision=12,scale=2) private BigDecimal price;
  @Column(length=1000) private String imageUrl;
  @Column(length=3000) private String description;
  @Column(nullable=false,updatable=false) private Instant createdAt;
  @Column(nullable=false) private Instant updatedAt;
  @PrePersist void created(){ createdAt=updatedAt=Instant.now(); }
  @PreUpdate void updated(){ updatedAt=Instant.now(); }
  public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
  public String getArtist(){return artist;} public void setArtist(String v){artist=v;}
  public String getCategory(){return category;} public void setCategory(String v){category=v;}
  public Integer getYearCreated(){return yearCreated;} public void setYearCreated(Integer v){yearCreated=v;}
  public String getMedium(){return medium;} public void setMedium(String v){medium=v;}
  public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
  public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
  public String getDescription(){return description;} public void setDescription(String v){description=v;}
  public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
