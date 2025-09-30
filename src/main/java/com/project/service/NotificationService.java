// package com.project.service;

// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;

// import com.project.entity.Notification;
// import com.project.entity.Tracking;
// import com.project.entity.Notification.NOTIFY_TYPE;
// import com.project.repository.NotificationRepository;
// import com.project.repository.TrackingRepository;
// import com.project.util.AlertUtil;
// import com.project.viewmodel.NotificationViewModel;

// import javafx.application.Platform;

// public class NotificationService {
//     private final TrackingRepository trackingRepository;
//     private final NotificationRepository notificationRepository;

//     public NotificationService() {
//         this.notificationRepository = new NotificationRepository();
//         this.trackingRepository = new TrackingRepository();
//     }

//     public List<Notification> getAllNotifications() {
//         return notificationRepository.findAll();
//     }

//     public void markAsRead(Notification notification) {
//         notification.setIsRead(true);
//         notificationRepository.save(notification);
//     }

//     public void markAllAsRead() {
//         List<Notification> notifications = notificationRepository.findAll().stream()
//                 .filter(n -> !n.getIsRead())
//                 .toList();
//         for (Notification n : notifications) {
//             n.setIsRead(true);
//             notificationRepository.save(n);
//         }
//     }

//     // create notification if there is any tracking that is in watching status and its
//     // schedule time is now

//     public List<Notification> createNotifications() {
//         LocalDateTime now = LocalDateTime.now(); // current date and time
//         LocalTime nowTime = now.toLocalTime(); // current time only
//         List<Notification> notifications = new ArrayList<>();
//         List<Tracking> watchingList = trackingRepository.findAll().stream()
//                 .filter(t -> t.getTrackingStatus() == Tracking.TRACKINGS_STATUS.WATCHING)
//                 .toList();

//         // create notification in day
//         for (Tracking t : watchingList) {
//             if (t.getScheduleDay().toString().equalsIgnoreCase(now.getDayOfWeek().toString())) {
//                 if (t.getScheduleTime().isBefore(nowTime.plusMinutes(1))
//                         && t.getScheduleTime().isAfter(nowTime.minusMinutes(1))) {
//                     // create notification
//                     Notification n = Notification.builder()
//                             // .title(t.getAnime().getTitle()+ " Release!")
//                             .title("New Episode with tracking_id: " + t.getId() + " Release!")
//                             .createdAt(now)
//                             .notifiableType(NOTIFY_TYPE.EPISODE_RELEASE)
//                             .isRead(false)
//                             .build();
//                     n.setTracking(t);
//                     notifications.add(n);
//                 }
//             }
//         }
//         return notifications;

//     }

// }
