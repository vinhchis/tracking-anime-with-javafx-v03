package com.project.service;

import com.project.entity.Notification;
import com.project.entity.Tracking;
import com.project.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService {
    private final JpaRepository<Notification, Integer> notificationRepository;
    private final Timer timer;

    public NotificationService(JpaRepository<Notification, Integer> notificationRepository) {
        this.notificationRepository = notificationRepository;
        this.timer = new Timer(true); // Sử dụng Timer daemon
    }

    /**
     * Creates and saves a new notification to the database.
     *
     * @param title The title of the notification.
     * @param tracking The associated tracking object.
     * @param type The type of notification (e.g., EPISODE_RELEASE).
     * @return The saved Notification object.
     */
    public Notification createAndSaveNotification(String title, Tracking tracking, Notification.NOTIFY_TYPE type) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setTracking(tracking);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setNotifiableType(type);

        return notificationRepository.save(notification);
    }

    /**
     * Schedules a notification to be sent at a specific time.
     * Note: This is a simple implementation for desktop. A more robust solution might use
     * a background service or a more advanced scheduling library.
     *
     * @param notification The notification object to be scheduled.
     */
    public void scheduleNotification(Notification notification) {
        // Calculate the delay until the notification time
        long delay = java.time.Duration.between(LocalDateTime.now(), notification.getCreatedAt()).toMillis();
        if (delay > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendNativeNotification(notification.getTitle(), "Bạn có một thông báo mới về anime: " + notification.getTracking().getAnime().getTitle());
                    // Mark the notification as read after sending
                    notification.setIsRead(true);
                    notificationRepository.save(notification);
                }
            }, delay);
        }
    }

    /**
     * Retrieves all unread notifications from the database.
     *
     * @return A list of unread notifications.
     */
    public List<Notification> getUnreadNotifications() {
        // Assuming findBy method exists to query based on a column
        return notificationRepository.findBy("isRead", false);
    }

    /**
     * Sends a native desktop notification.
     *
     * @param title The notification title.
     * @param message The notification message.
     */
    private void sendNativeNotification(String title, String message) {
        // TODO: Implement native notification logic using Java 9+ Desktop API or other libraries.
        System.out.println("Gửi thông báo Native:\nTiêu đề: " + title + "\nNội dung: " + message);
    }
}
