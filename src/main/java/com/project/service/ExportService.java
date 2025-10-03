package com.project.service;

import com.project.entity.Tracking;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service chịu trách nhiệm xuất lịch chiếu thành file iCalendar (.ics).
 * Các sự kiện lặp lại hàng tuần.
 */
public class ExportService {

    private final TrackingService trackingService;
    // Định dạng ngày giờ chuẩn UTC cho file ICS
    private static final DateTimeFormatter ICS_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    public ExportService(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    /**
     * Xuất lịch chiếu các anime đã được lên lịch thành file .ics.
     * @param outputPath Đường dẫn file sẽ được lưu (ví dụ: "C:/Users/User/Desktop/my_schedule.ics")
     * @throws IOException
     */
    public void exportScheduleToIcs(Path outputPath) throws IOException {
        // Sử dụng TrackingRepository trực tiếp vì TrackingService đã mở public cho trackingRepository
        // Nếu không, ta cần thêm phương thức vào TrackingService để lấy List<Tracking>
        List<Tracking> scheduledTrackings = trackingService.trackingRepository.getScheduledTrackings();

        if (scheduledTrackings.isEmpty()) {
            System.out.println("Không tìm thấy anime nào được đặt lịch để xuất file.");
            return;
        }

        StringBuilder icsContent = new StringBuilder();

        // Header của file iCalendar
        icsContent.append("BEGIN:VCALENDAR\r\n");
        icsContent.append("VERSION:2.0\r\n");
        icsContent.append("PRODID:-//TrackingAnimeApp//NONSGML v1.0//EN\r\n");
        icsContent.append("CALSCALE:GREGORIAN\r\n");
        icsContent.append("METHOD:PUBLISH\r\n");

        // Lấy múi giờ hệ thống của người dùng
        ZoneId localZone = ZoneId.systemDefault();

        for (Tracking t : scheduledTrackings) {
            // Chuẩn bị thời gian (Cần một điểm bắt đầu để đặt RRULE)
            // Ta dùng ngày hiện tại và điều chỉnh đến ngày chiếu trong tuần (scheduleDay)
            LocalDateTime scheduleTime = LocalDateTime.now()
                    // Đảm bảo là ngày chiếu đúng trong tuần
                    .with(java.time.DayOfWeek.valueOf(t.getScheduleDay().name()))
                    // Thiết lập giờ chiếu
                    .with(t.getScheduleTime());

            // Chuyển sang ZonedDateTime và chuẩn hóa về UTC để đồng bộ lịch
            ZonedDateTime zdt = scheduleTime.atZone(localZone).withZoneSameInstant(ZoneId.of("UTC"));
            String dtStart = zdt.format(ICS_DATE_FORMAT);
            String dtStamp = ZonedDateTime.now(ZoneId.of("UTC")).format(ICS_DATE_FORMAT);

            icsContent.append("BEGIN:VEVENT\r\n");
            // UID: ID duy nhất cho sự kiện
            icsContent.append("UID:").append(t.getId()).append("-").append(dtStamp).append("@tracking-anime\r\n");
            icsContent.append("DTSTAMP:").append(dtStamp).append("\r\n");
            icsContent.append("SUMMARY:").append(t.getAnime().getTitle()).append(" (Tập mới)\r\n");
            icsContent.append("DESCRIPTION:").append(t.getNote() != null ? t.getNote() : "Lịch chiếu tập tiếp theo").append("\r\n");

            // DTSTART: Thời điểm bắt đầu của sự kiện lặp lại
            icsContent.append("DTSTART:").append(dtStart).append("\r\n");

            // RRULE: Thiết lập quy tắc lặp lại hàng tuần (FREQ=WEEKLY) vào đúng ngày chiếu (BYDAY)
            // BYDAY dùng định dạng 2 ký tự (MO, TU, WE, TH, FR, SA, SU)
            icsContent.append("RRULE:FREQ=WEEKLY;BYDAY=").append(t.getScheduleDay().name().substring(0, 2)).append("\r\n");

            icsContent.append("END:VEVENT\r\n");
        }

        // Footer của file iCalendar
        icsContent.append("END:VCALENDAR\r\n");

        // Ghi nội dung ra file
        try (FileWriter writer = new FileWriter(outputPath.toFile())) {
            writer.write(icsContent.toString());
        }
        System.out.println("Đã xuất lịch chiếu thành công ra: " + outputPath.toAbsolutePath());
    }
}