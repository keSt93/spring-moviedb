package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Genre;
import keSt93.springmoviedb.entities.Series;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends PagingAndSortingRepository<Series, Integer> {

    public Series findById(int id);
}
