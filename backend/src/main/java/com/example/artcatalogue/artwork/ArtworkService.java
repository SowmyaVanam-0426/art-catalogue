package com.example.artcatalogue.artwork;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
public class ArtworkService {
  private final ArtworkRepository repo;
  public ArtworkService(ArtworkRepository repo){this.repo=repo;}
  public List<Artwork> list(String search,String category){
    if(search!=null&&!search.isBlank()) return repo.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrderByCreatedAtDesc(search.trim(),search.trim());
    if(category!=null&&!category.isBlank()) return repo.findByCategoryIgnoreCaseOrderByCreatedAtDesc(category.trim());
    return repo.findAllByOrderByCreatedAtDesc();
  }
  public Artwork get(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Artwork not found"));}
  public Artwork create(ArtworkRequest r){Artwork a=new Artwork(); return repo.save(apply(a,r));}
  public Artwork update(Long id,ArtworkRequest r){return repo.save(apply(get(id),r));}
  public void delete(Long id){repo.delete(get(id));}
  private Artwork apply(Artwork a,ArtworkRequest r){
    a.setTitle(r.title().trim()); a.setArtist(r.artist().trim()); a.setCategory(r.category().trim());
    a.setYearCreated(r.yearCreated()); a.setMedium(r.medium()); a.setPrice(r.price());
    a.setImageUrl(r.imageUrl()); a.setDescription(r.description()); return a;
  }
}
