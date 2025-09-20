package com.project.service;

import com.project.entity.Tracking;
import com.project.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsService {

    private final JpaRepository<Tracking, Integer> trackingRepository;

    // Constructor yêu cầu repository
    public StatisticsService(JpaRepository<Tracking, Integer> trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    /**
     * Đếm tổng số anime đã được theo dõi
     */
    public long countAllTrackedAnime() {
        return trackingRepository.countAll();
    }

    /**
     * Đếm số anime theo trạng thái (COMPLETED, ONGOING, ...)
     */
    public Map<Tracking.TrackingStatus, Long> countByStatus() {
        List<Tracking> allTrackings = trackingRepository.findAll();
        return allTrackings.stream()
                .collect(Collectors.groupingBy(Tracking::getTrackingStatus, Collectors.counting()));
    }

    /**
     * Tính tỷ lệ hoàn thành
     */
    public double calculateCompletionRate() {
        long total = countAllTrackedAnime();
        if (total == 0) {
            return 0.0;
        }
        long completed = trackingRepository.findByStatus(Tracking.TrackingStatus.COMPLETED).size();
        return (double) completed / total * 100;
    }

    // --- Các method khớp với OverviewController ---

    /**
     * Tổng số anime
     */
    public long getTotalAnimeCount() {
        return countAllTrackedAnime();
    }

    /**
     * Đếm số anime theo status (dùng string)
     */
    public long getAnimeCountByStatus(String status) {
        try {
            Tracking.TrackingStatus ts = Tracking.TrackingStatus.valueOf(status.toUpperCase());
            return countByStatus().getOrDefault(ts, 0L);
        } catch (IllegalArgumentException e) {
            return 0L; // nếu status không hợp lệ
        }
    }

    /**
     * Thống kê theo thể loại (genre)
     * ⚠️ giả định trong Tracking → Anime có field genre (String).
     * Nếu chưa có thì cần bổ sung field này.
     */
    public Map<String, Long> getGenreStatistics() {
        return trackingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getAnime().getGenre(), Collectors.counting()
                ));
    }

    /**
     * Thống kê theo mùa (season)
     * ⚠️ giả định trong Tracking → Anime có field season (Season entity).
     */
    public Map<String, Long> getSeasonStatistics() {
        return trackingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        t -> t.getAnime().getSeason().toString(), Collectors.counting()
                ));
    }
}