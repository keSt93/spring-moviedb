package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    User findByUsernameEquals(String username);

    List<User> findAllByIdNot(int idNot);
}
