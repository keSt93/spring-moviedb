package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieRating;
import keSt93.springmoviedb.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends PagingAndSortingRepository<MovieRating, Integer> {
    public Iterable<MovieRating> findAllByMovie(Movie m);

    @Query("select sum(rating) from MovieRating where movie = ?1")
    public int getCalculatedMovieRatingForMovie(Movie movie);

    @Query("select count(rating) from MovieRating where movie = ?1")
    public int getCountOfMovieRatingsForMovie(Movie movie);

    public MovieRating findByUserAndAndMovie(User user, Movie movie);

}
