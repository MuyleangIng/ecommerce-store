package co.cstad.sen.api.notification;


import co.cstad.sen.api.notification.web.CreateNotificationDto;

public interface NotificationService {
   boolean pushNotification(CreateNotificationDto notificationDto);
}