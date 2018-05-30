package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.NotificationType;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationUserRelationRepository extends PagingAndSortingRepository<NotificationUserRelation, Integer> {

    Iterable<NotificationUserRelation> findAllByUserAndIsSeenFalse(User user);
    Iterable<NotificationUserRelation> findTop5ByUserOrderByIdDesc(User user);

}
