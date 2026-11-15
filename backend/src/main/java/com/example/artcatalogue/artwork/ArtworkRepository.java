package com.example.artcatalogue.artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ArtworkRepository extends JpaRepository<Artwork,Long> {
  List<Artwork> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrderByCreatedAtDesc(String title,String artist);
  List<Artwork> findAllByOrderByCreatedAtDesc();
  List<Artwork> findByCategoryIgnoreCaseOrderByCreatedAtDesc(String category);
}
