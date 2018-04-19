package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.UserRoles;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends PagingAndSortingRepository<UserRoles, Integer> {

}
