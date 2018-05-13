package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    public Movie findById(int id);

    public Page<Movie> findAllByOrderByIdDesc(Pageable pageable);

    public Iterable<Movie> findFirst6ByOrderByRatingDesc();
    public Iterable<Movie> findFirst14ByOrderByRegisteredDateDesc();
    public Iterable<Movie> findAllBySeries(Series series);
    public Iterable<Movie> findAllBySeriesAndIdIsNot (Series series, int id);


    @Query("select sum(length) from Movie")
    public int getTotalWastedMinutes();

    @Query(value = "select sum(length) from movies where series_id = ?1", nativeQuery = true)
    public int getTotalSeriesWastedTime(Series series);

    @Query(value = "select sum(rating)/count(id) from movies where series_id = ?1", nativeQuery = true)
    public double getAverageSeriesRating(Series series);

    public int countAllBy();
    public int countAllBySeriesEquals(Series Series);


    @Modifying
    @Transactional
    @Query("update Movie set rating = ?1 where id = ?2")
    int updateMovieRating(Double rating, int id);






    @Query("select count(id) from Movie where genre = 1")
    public int getTotalHorrorMoviesSum();

    @Query("select count(id) from Movie where genre = 2")
    public int getTotalActionMoviesSum();

    @Query("select count(id) from Movie where genre = 3")
    public int getTotalComedyMoviesSum();






}
