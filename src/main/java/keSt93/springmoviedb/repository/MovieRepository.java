package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    public Iterable<Movie> findAllByOrderByIdDesc();

    public Iterable<Movie> findFirst4ByOrderByRatingDesc();

    public Iterable<Movie> findFirst4ByOrderByRegisteredDateDesc();

}
