package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends PagingAndSortingRepository<Series, Integer> {

    Series findById(int id);

    Iterable<Series> findByIdOrderByIdDesc(int id);

    Iterable<Series> findFirst3ByOrderByLastUsedDesc();

    Page<Series> findAllByIdNotOrderByIdDesc(int id, Pageable pageable);

}
