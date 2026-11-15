package com.example.artcatalogue.artwork;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {
  private final ArtworkService service;
  public ArtworkController(ArtworkService service){this.service=service;}
  @GetMapping public List<Artwork> list(@RequestParam(required=false) String search,@RequestParam(required=false) String category){return service.list(search,category);}
  @GetMapping("/{id}") public Artwork get(@PathVariable Long id){return service.get(id);}
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Artwork create(@Valid @RequestBody ArtworkRequest r){return service.create(r);}
  @PutMapping("/{id}") public Artwork update(@PathVariable Long id,@Valid @RequestBody ArtworkRequest r){return service.update(id,r);}
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}
}
