package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {

    Movie findById(int id);

    Page<Movie> findAllByOrderByIdDesc(Pageable pageable);

    Iterable<Movie> findFirst6ByOrderByRatingDesc();
    Iterable<Movie> findFirst11ByOrderByRegisteredDateDesc();
    Iterable<Movie> findAllBySeries(Series series);
    Iterable<Movie> findAllBySeriesAndIdIsNot (Series series, int id);


    @Query("select sum(length) from Movie")
    int getTotalWastedMinutes();

    @Query(value = "select sum(length) from movies where series_id = ?1", nativeQuery = true)
    int getTotalSeriesWastedTime(Series series);

    @Query(value = "select sum(rating)/count(id) from movies where series_id = ?1", nativeQuery = true)
    double getAverageSeriesRating(Series series);

    int countAllBy();
    int countAllBySeriesEquals(Series Series);


    @Modifying
    @Transactional
    @Query("update Movie set rating = ?1 where id = ?2")
    int updateMovieRating(Double rating, int id);

    @Query("select count(id) from Movie where genre = 1")
    int getTotalHorrorMoviesSum();

    @Query("select count(id) from Movie where genre = 2")
    int getTotalActionMoviesSum();

    @Query("select count(id) from Movie where genre = 3")
    int getTotalComedyMoviesSum();
}
