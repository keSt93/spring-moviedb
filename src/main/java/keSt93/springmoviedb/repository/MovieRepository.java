package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    public Iterable<Movie> findAllByOrderByIdDesc();

    public Iterable<Movie> findFirst8ByOrderByRatingDesc();

    public Iterable<Movie> findFirst8ByOrderByRegisteredDateDesc();

    @Query("select sum(length) from Movie")
    public int getTotalWastedMinutes();

    @Query("select count(id) from Movie")
    public int getTotalMovies();

    @Query("select count(id) from Movie where genre = 1")
    public int getTotalHorrorMoviesSum();

    @Query("select count(id) from Movie where genre = 2")
    public int getTotalActionMoviesSum();

    @Query("select count(id) from Movie where genre = 3")
    public int getTotalComedyMoviesSum();


}
