package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Genre;
import keSt93.springmoviedb.entities.Notification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Integer> {

}
