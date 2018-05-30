package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, Integer> {

    Genre findByName(String name);
}
