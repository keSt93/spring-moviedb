package keSt93.springmoviedb.repository;


import keSt93.springmoviedb.entities.Notification;
import keSt93.springmoviedb.entities.NotificationType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends PagingAndSortingRepository<NotificationType, Integer> {
    public int NOTIFICATION_NEWMOVIE = 1;

    public NotificationType findById(int id);
}
