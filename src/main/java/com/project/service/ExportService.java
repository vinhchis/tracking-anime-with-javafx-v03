// src/main/java/com/project/service/ExportService.java

package com.project.service;

import com.project.entity.Tracking;
import com.project.repository.TrackingRepository; // <<< Cần import
import org.springframework.stereotype.Service; // <<< Cần import để quản lý Service
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service // Thêm Service Annotation
public class ExportService {

    private final TrackingRepository trackingRepository; // <<< THÊM: Dependency
    private static final DateTimeFormatter ICS_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    private static final int EVENT_DURATION_MINUTES = 30; // Giả định mỗi tập dài 30 phút

    // Constructor mới để nhận TrackingRepository
    public ExportService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    /**
     * Xuất lịch chiếu của tất cả các bộ đang được theo dõi và có showtime ra file .ics.
     * Phương thức này sẽ lấy dữ liệu từ Repository thay vì nhận qua tham số.
     *
     * @param filePath The file path to save the .ics file.
     * @throws IOException If an I/O error occurs.
     */
    public void exportTimetableToIcs(String filePath) throws IOException {
        // Lấy danh sách các bản ghi Tracking có lịch chiếu
        List<Tracking> trackingList = trackingRepository.findByShowtimeIsNotNullOrderByShowtimeAsc();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("BEGIN:VCALENDAR\n");
            writer.write("VERSION:2.0\n");
            writer.write("PRODID:-//TrackingAnimeApp//NONSGML v1.0//EN\n");

            for (Tracking tracking : trackingList) {
                writer.write(createVEvent(tracking));
            }
            writer.write("END:VCALENDAR\n");
        }
    }

    /**
     * Tạo một sự kiện VEVENT (lịch) cho mỗi bộ anime, bao gồm luật lặp lại hàng tuần.
     */
    private String createVEvent(Tracking tracking) {
        StringBuilder event = new StringBuilder();
        LocalDateTime showtime = tracking.getShowtime();

        if (showtime == null) return "";

        // 1. Tính toán thời gian bắt đầu (DTSTART)
        LocalDateTime startTime = getNextRecurrenceTime(showtime);
        LocalDateTime endTime = startTime.plusMinutes(EVENT_DURATION_MINUTES);

        // 2. Định dạng ngày trong tuần cho RRULE (Ví dụ: MON, TUE)
        String dayOfWeekCode = showtime.getDayOfWeek().toString().substring(0, 2);

        event.append("BEGIN:VEVENT\n");
        // UID phải là duy nhất, dùng ID của Tracking + thời gian để đảm bảo duy nhất
        event.append("UID:").append(tracking.getId()).append("-").append(startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))).append("@tracking-anime.com\n");
        event.append("DTSTAMP:").append(LocalDateTime.now().format(ICS_DATE_FORMAT)).append("\n");

        // DTSTART/DTEND là mốc thời gian cụ thể (cần là UTC nếu không muốn lỗi)
        event.append("DTSTART:").append(startTime.format(ICS_DATE_FORMAT)).append("\n");
        event.append("DTEND:").append(endTime.format(ICS_DATE_FORMAT)).append("\n");

        // RRULE: Thiết lập sự kiện lặp lại hàng tuần vào ngày chiếu cố định
        event.append("RRULE:FREQ=WEEKLY;BYDAY=").append(dayOfWeekCode).append("\n");

        event.append("SUMMARY:Anime Release: ").append(tracking.getAnime().getTitle()).append("\n");
        event.append("DESCRIPTION:Tập tiếp theo: ").append(tracking.getLastWatchedEpisode() + 1)
                .append(". Trạng thái: ").append(tracking.getTrackingStatus())
                .append(". Ghi chú: ").append(tracking.getNote() != null ? tracking.getNote() : "Không có").append("\n");
        event.append("LOCATION:Phát sóng\n");
        event.append("END:VEVENT\n");

        return event.toString();
    }

    /**
     * Tìm thời điểm lặp lại gần nhất để đặt làm DTSTART.
     */
    private LocalDateTime getNextRecurrenceTime(LocalDateTime showtime) {
        LocalDateTime now = LocalDateTime.now();

        // Đặt giờ và phút của showtime vào ngày hiện tại
        LocalDateTime nextTime = now.withHour(showtime.getHour())
                .withMinute(showtime.getMinute())
                .withSecond(0)
                .withNano(0);

        // Dịch chuyển đến ngày trong tuần của showtime (DayOfWeek)
        int dayOfWeekTarget = showtime.getDayOfWeek().getValue();
        int dayOfWeekCurrent = nextTime.getDayOfWeek().getValue();

        if (dayOfWeekTarget > dayOfWeekCurrent) {
            nextTime = nextTime.plusDays(dayOfWeekTarget - dayOfWeekCurrent);
        } else if (dayOfWeekTarget < dayOfWeekCurrent) {
            nextTime = nextTime.plusDays(7 - (dayOfWeekCurrent - dayOfWeekTarget));
        }

        // Nếu thời điểm đã qua trong tuần này, chuyển sang tuần sau
        if (nextTime.isBefore(now)) {
            nextTime = nextTime.plusWeeks(1);
        }

        return nextTime;
    }
}