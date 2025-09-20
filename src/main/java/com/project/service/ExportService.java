package com.project.service;

import com.project.entity.Tracking;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    /**
     * Exports a list of tracking data to an .ics calendar file.
     *
     * @param trackingList The list of tracking objects to export.
     * @param filePath The file path to save the .ics file.
     * @throws IOException If an I/O error occurs.
     */
    public void exportToIcs(List<Tracking> trackingList, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("BEGIN:VCALENDAR\n");
            writer.write("VERSION:2.0\n");
            writer.write("PRODID:-//MyAnimeTracker//NONSGML v1.0//EN\n");

            // Format for ICS DTSTAMP and DTSTART
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

            for (Tracking tracking : trackingList) {
                // Only export if showtime is set
                if (tracking.getShowtime() != null) {
                    writer.write("BEGIN:VEVENT\n");
                    writer.write("UID:" + tracking.getId() + "@myanimetracker.com\n");
                    writer.write("DTSTAMP:" + LocalDateTime.now().format(formatter) + "\n");
                    writer.write("DTSTART:" + tracking.getShowtime().format(formatter) + "\n");
                    writer.write("SUMMARY:Anime Release: " + tracking.getAnime().getTitle() + "\n");
                    writer.write("DESCRIPTION:Thời gian chiếu: " + tracking.getShowtime().toString() + "\n");
                    writer.write("LOCATION:Phát sóng trên TV/Streaming\n");
                    writer.write("END:VEVENT\n");
                }
            }
            writer.write("END:VCALENDAR\n");
        }
    }
}
