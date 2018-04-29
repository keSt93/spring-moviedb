package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Genre;
import keSt93.springmoviedb.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends PagingAndSortingRepository<Series, Integer> {

    public Series findById(int id);

    public Iterable<Series> findByIdOrderByIdDesc(int id);

    public Iterable<Series> findFirst2ByOrderByLastUsedDesc();

    public Page<Series> findAllByIdNotOrderByIdDesc(int id, Pageable pageable);

}
