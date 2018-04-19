package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.MovieComments;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCommentsRepository extends PagingAndSortingRepository<MovieComments, Integer> {

}
