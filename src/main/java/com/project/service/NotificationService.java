package com.project.service;

import com.project.entity.Notification;
import com.project.entity.Tracking;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.repository.NotificationRepository;
import com.project.repository.TrackingRepository;
import com.project.util.JpaUtil;

import jakarta.persistence.EntityManager; // ĐÃ THÊM IMPORT
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class NotificationService {

    private final TrackingRepository trackingRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(TrackingRepository trackingRepository, NotificationRepository notificationRepository) {
        this.trackingRepository = trackingRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * Chạy định kỳ để kiểm tra các anime sắp chiếu và tạo/gửi thông báo.
     * Cần được gọi bằng một Timer/Scheduler (ví dụ: trong Main hoặc DashboardController).
     */
    public void checkForScheduleNotifications() {
        LocalDateTime now = LocalDateTime.now();

        // Chuyển đổi DayOfWeek của Java sang Enum DAY_OF_WEEK của dự án
        DAY_OF_WEEK today = DAY_OF_WEEK.valueOf(now.getDayOfWeek().name().toUpperCase(Locale.ROOT));

        // 1. Lấy tất cả Tracking có đặt lịch chiếu (dùng phương thức Lộc đã tạo)
        List<Tracking> scheduledTrackings = trackingRepository.getScheduledTrackings();

        for (Tracking tracking : scheduledTrackings) {
            // Kiểm tra xem Tracking có phải chiếu hôm nay không
            if (tracking.getScheduleDay() == today) {
                // Lấy thời gian chiếu đã đặt
                LocalTime scheduledTime = tracking.getScheduleTime();

                // Lấy thời gian hiện tại chỉ tính giờ/phút/giây
                LocalTime currentTime = now.toLocalTime();

                // Thiết lập cửa sổ thông báo: 15 phút trước giờ chiếu
                LocalTime notificationTimeStart = scheduledTime.minusMinutes(15);

                // Kiểm tra: Nếu thời gian hiện tại nằm trong cửa sổ [notificationTimeStart, scheduledTime]
                if (currentTime.isAfter(notificationTimeStart) && currentTime.isBefore(scheduledTime)) {

                    // 2. Tạo thông báo
                    String title = tracking.getAnime().getTitle();
                    String message = String.format("%s sẽ chiếu tập tiếp theo lúc %s hôm nay!",
                            title, scheduledTime.toString());

                    Notification newNotification = Notification.builder()
                            .title(message)
                            .createdAt(now)
                            .isRead(false)
                            .tracking(tracking)
                            .notifiableType(Notification.NOTIFY_TYPE.EPISODE_RELEASE)
                            .build();

                    // 3. Lưu thông báo vào DB
                    notificationRepository.save(newNotification);

                    // 4. Gửi thông báo Native (SỬA LỖI: Thay thế NotificationUtil bằng System.out.println)
                    System.out.println("[NATIVE NOTIFICATION] " + message);
                }
            }
        }
    }

    // Phương thức lấy tất cả thông báo chưa đọc
    public List<Notification> getAllUnreadNotifications() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            // SỬA LỖI: Tạo Query
            return em.createQuery("SELECT n FROM Notification n WHERE n.isRead = FALSE ORDER BY n.createdAt DESC", Notification.class)
                    .getResultList();
        } finally {
            // SỬA LỖI: Đóng EntityManager an toàn
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Phương thức đánh dấu đã đọc
    public void markAsRead(Notification notification) {
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}