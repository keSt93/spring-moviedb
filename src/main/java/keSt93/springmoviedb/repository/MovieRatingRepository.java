package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends PagingAndSortingRepository<MovieRating, Integer> {
    public Iterable<MovieRating> findAllByMovie(Movie m);
}
