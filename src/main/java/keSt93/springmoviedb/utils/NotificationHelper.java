package keSt93.springmoviedb.utils;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.Notification;
import keSt93.springmoviedb.entities.NotificationUserRelation;
import keSt93.springmoviedb.entities.User;
import keSt93.springmoviedb.repository.NotificationTypeRepository;
import keSt93.springmoviedb.repository.NotificationUserRelationRepository;
import keSt93.springmoviedb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.Date;
import java.util.List;

public class NotificationHelper {

    public int NOTIFICATION_NEWMOVIE = 1;

    UserRepository userRepository;
    NotificationUserRelationRepository notificationUserRelationRepository;
    NotificationTypeRepository notificationTypeRepository;

    public NotificationHelper(UserRepository userRepository, NotificationUserRelationRepository notificationUserRelationRepository) {
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
    }

    public NotificationHelper(UserRepository userRepository, NotificationUserRelationRepository notificationUserRelationRepository, NotificationTypeRepository notificationTypeRepository) {
        this.userRepository = userRepository;
        this.notificationUserRelationRepository = notificationUserRelationRepository;
        this.notificationTypeRepository = notificationTypeRepository;
    }

    public Iterable<NotificationUserRelation> getNotificationsFromUser(Principal principal) {


        Iterable<NotificationUserRelation> returnValue;
        try {
            User user = userRepository.findByUsernameEquals(principal.getName());
           returnValue = notificationUserRelationRepository.findAllByUserAndIsSeenFalse(user);
        } catch (NullPointerException npe) {
            returnValue = null;
        }
        return returnValue;
    }

    public void createNotification(
            User fromUser,
            String notificationText,
            String notificationUrl,
            String notificationImage)
    {
        Notification notification = new Notification();

        notification.setNotificationText(notificationText);
        notification.setNotificationUrl(notificationUrl);
        notification.setNotificationImage(notificationImage);

        notification.setNotificationDate(new Date());

        List<User> noteworthyUsers = userRepository.findAllByIdNot(fromUser.getId());
        for (User noteworthyUser : noteworthyUsers) {
            NotificationUserRelation relation = new NotificationUserRelation();
            relation.setNotification(notification);
            relation.setUser(noteworthyUser);
            relation.setSeen(false);
            notificationUserRelationRepository.save(relation);
        }
    }


}
