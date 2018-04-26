package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.NotificationType;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUserRelationRepository extends PagingAndSortingRepository<NotificationUserRelation, Integer> {

}
