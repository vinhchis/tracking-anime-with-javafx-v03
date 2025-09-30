// package com.project.viewmodel;

// import java.time.Duration;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;

// import com.project.entity.Notification;
// import com.project.service.NotificationService;

// import javafx.application.Platform;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;

// public class NotificationViewModel {
//     private final ObservableList<Notification> events = FXCollections.observableArrayList();
//     private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//     private final NotificationService notificationService;

//     public NotificationViewModel() {
//         notificationService = new NotificationService();
//         events.addAll(notificationService.getAllNotifications());
//     }



//     public void start() {
//         scheduler.scheduleAtFixedRate(() -> {
//             LocalDateTime now = LocalDateTime.now();


//         }, 0, 1, TimeUnit.SECONDS); // check mỗi giây
//     }

//     // public void schedule() {
//     //     List<Notification> newNotifications = fetchDueNotifications();
//     //     for (Notification n : newNotifications) {
//     //         viewModel.addEvent(n);
//     //         scheduleNotification(n);
//     //     }
//     // }

//     // public void scheduleNotification(Notification event) {
//     //     long delay = Duration.between(LocalDateTime.now(), event.getTime()).toMillis();
//     //     if (delay < 0) return; // đã qua rồi thì bỏ qua

//     //     scheduler.schedule(() -> {

//     //     }, delay, TimeUnit.MILLISECONDS);
//     // }



//       public void stop() {
//         scheduler.shutdownNow();
//     }


//      public ObservableList<Notification> getEvents() {
//         return events;
//     }

//     public void addEvent(Notification event) {
//         events.add(event);
//     }
// }
