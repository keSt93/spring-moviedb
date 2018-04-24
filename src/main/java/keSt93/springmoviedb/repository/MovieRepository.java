package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Series;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    public Iterable<Movie> findAllByOrderByIdDesc();

    public Iterable<Movie> findFirst8ByOrderByRatingDesc();

    public Iterable<Movie> findFirst8ByOrderByRegisteredDateDesc();

    public Iterable<Movie> findFirst10BySeriesIsNot(Series isNot);

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

    public Iterable<Movie> findAllBySeries(Series series);


}
