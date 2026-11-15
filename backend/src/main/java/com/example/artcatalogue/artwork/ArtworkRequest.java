package com.example.artcatalogue.artwork;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record ArtworkRequest(
 @NotBlank @Size(max=160) String title,
 @NotBlank @Size(max=120) String artist,
 @NotBlank @Size(max=80) String category,
 @Min(1000) @Max(2100) Integer yearCreated,
 @Size(max=120) String medium,
 @DecimalMin("0.0") @Digits(integer=10,fraction=2) BigDecimal price,
 @Size(max=1000) String imageUrl,
 @Size(max=3000) String description) {}
