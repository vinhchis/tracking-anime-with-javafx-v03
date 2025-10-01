// src/main/java/com/project/service/NotificationService.java

package com.project.service;

import com.project.entity.Notification;
import com.project.entity.Tracking;
import com.project.repository.JpaRepository;
import com.project.repository.TrackingRepository; // <<< Cần import
import com.project.entity.Tracking.TrackingStatus; // Cần import
import com.project.util.AlertUtil; // Giả định cần để gửi thông báo native

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Optional; // Cần thiết cho một số hàm repository

public class NotificationService {
    private final JpaRepository<Notification, Integer> notificationRepository;
    private final TrackingRepository trackingRepository; // <<< THÊM: Repository để lấy lịch chiếu
    private final Timer timer;

    // Khoảng thời gian kiểm tra (ví dụ: mỗi 1 phút)
    private static final long CHECK_INTERVAL_MS = 60 * 1000;
    // Thời gian thông báo trước (ví dụ: 5 phút trước giờ chiếu)
    private static final int NOTIFY_BEFORE_MINUTES = 5;

    // Constructor mới, nhận thêm TrackingRepository
    public NotificationService(
            JpaRepository<Notification, Integer> notificationRepository,
            TrackingRepository trackingRepository // <<< THÊM PARAM
    ) {
        this.notificationRepository = notificationRepository;
        this.trackingRepository = trackingRepository; // <<< Gán giá trị
        this.timer = new Timer(true);

        startShowtimeChecker(); // Bắt đầu kiểm tra lịch khi Service khởi tạo
    }

    // --- PHƯƠNG THỨC MỚI CỦA LOC ---

    /**
     * Bắt đầu tác vụ chạy nền kiểm tra anime sắp chiếu.
     */
    public void startShowtimeChecker() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndScheduleNotifications();
            }
        }, 0, CHECK_INTERVAL_MS);
    }

    /**
     * Logic kiểm tra các anime sắp chiếu dựa trên showtime đã thiết lập.
     * Sử dụng dữ liệu Tracking có showtime (từ TrackingRepositoryImpl)
     */
    public void checkAndScheduleNotifications() {
        // Chỉ lấy những bộ đang ở trạng thái WATCHING
        List<Tracking> watchingList = trackingRepository.findByStatus(TrackingStatus.WATCHING);

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime().withSecond(0).withNano(0);

        for (Tracking tracking : watchingList) {
            LocalDateTime showtime = tracking.getShowtime();

            // Bỏ qua nếu không có lịch chiếu
            if (showtime == null) continue;

            DayOfWeek scheduledDay = showtime.getDayOfWeek();
            LocalTime scheduledTime = showtime.toLocalTime().withSecond(0).withNano(0);

            // 1. Kiểm tra Ngày trong tuần
            if (scheduledDay.equals(today)) {

                // 2. Tính thời điểm cần thông báo (ví dụ: trước 5 phút)
                LocalTime notificationTime = scheduledTime.minusMinutes(NOTIFY_BEFORE_MINUTES);

                // 3. Kiểm tra xem có đang nằm trong khoảng thời gian cần thông báo không
                // (Từ NotificationTime đến ScheduledTime)
                if (!currentTime.isBefore(notificationTime) && currentTime.isBefore(scheduledTime)) {

                    String title = "SẮP CHIẾU: " + tracking.getAnime().getTitle();
                    // Giả định tập tiếp theo là tập đã xem + 1
                    short nextEpisode = (short) (tracking.getLastWatchedEpisode() + 1);
                    String message = String.format("Tập %d chiếu lúc %s hôm nay. Chuẩn bị xem!",
                            nextEpisode, scheduledTime.toString());

                    // 4. Tạo và Lưu thông báo vào CSDL
                    createAndSaveNotification(title, tracking, Notification.NOTIFY_TYPE.EPISODE_RELEASE);

                    // 5. Gửi thông báo tức thời
                    sendNativeNotification(title, message);
                }
            }
        }
    }

    // --- CÁC PHƯƠNG THỨC CŨ (giữ nguyên hoặc chỉnh sửa) ---

    // [Phương thức đã có]
    public Notification createAndSaveNotification(String title, Tracking tracking, Notification.NOTIFY_TYPE type) {
        // ... (Giữ nguyên logic)
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setTracking(tracking);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setNotifiableType(type);

        return notificationRepository.save(notification);
    }

    // [Phương thức đã có]
    public void scheduleNotification(Notification notification) {
        // Phương thức này có thể không cần dùng nữa nếu ta dùng checker định kỳ
        // Nhưng giữ lại để phòng trường hợp cần lên lịch thông báo 1 lần
        // ... (Giữ nguyên logic)
    }

    // [Phương thức đã có]
    public List<Notification> getUnreadNotifications() {
        // ... (Giữ nguyên logic)
        return notificationRepository.findBy("isRead", false);
    }

    // [Phương thức đã có - NATIVE]
    private void sendNativeNotification(String title, String message) {
        // TODO: Cần sử dụng AlertUtil.showNotification(title, message) nếu file đó tồn tại.
        // Tạm thời dùng System.out.println
        System.out.println("Gửi thông báo Native:\nTiêu đề: " + title + "\nNội dung: " + message);
    }

    /**
     * Dừng Timer khi ứng dụng đóng
     */
    public void shutdown() {
        timer.cancel();
    }
}