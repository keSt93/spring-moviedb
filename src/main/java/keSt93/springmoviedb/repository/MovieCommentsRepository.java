package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieComments;
import keSt93.springmoviedb.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCommentsRepository extends PagingAndSortingRepository<MovieComments, Integer> {

    Iterable<MovieComments> findAllByMovieOrderByCreationDateDesc(Movie movie);

    Iterable<MovieComments> findFirst5ByUserOrderByCreationDateDesc(User user);
}
