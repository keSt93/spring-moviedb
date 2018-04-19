package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.MovieRating;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends PagingAndSortingRepository<MovieRating, Integer> {

}
