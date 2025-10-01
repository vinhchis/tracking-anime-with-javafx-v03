// src/main/java/com/project/service/StatisticsService.java

package com.project.service;

import com.project.entity.Tracking;
import com.project.entity.Tracking.TrackingStatus;
import com.project.repository.TrackingRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final TrackingRepository trackingRepository;

    public StatisticsService(TrackingRepository trackingRepository) {
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
     * SỬ DỤNG TRUY VẤN TỐI ƯU TỪ REPOSITORY
     */
    public Map<TrackingStatus, Long> countByStatus() {
        Map<TrackingStatus, Long> stats = new EnumMap<>(TrackingStatus.class);
        for (TrackingStatus status : TrackingStatus.values()) {
            stats.put(status, 0L);
        }

        List<Object[]> rawData = trackingRepository.countTrackingByStatus();

        for (Object[] item : rawData) {
            TrackingStatus status = (TrackingStatus) item[0];
            Long count = (Long) item[1];
            stats.put(status, count);
        }

        return stats;
    }

    /**
     * Tính tỷ lệ hoàn thành
     */
    public double calculateCompletionRate() {
        long total = countAllTrackedAnime();
        if (total == 0) {
            return 0.0;
        }
        long completed = countByStatus().getOrDefault(TrackingStatus.COMPLETED, 0L);
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
            TrackingStatus ts = TrackingStatus.valueOf(status.toUpperCase());
            return countByStatus().getOrDefault(ts, 0L);
        } catch (IllegalArgumentException e) {
            return 0L;
        }
    }

    // [❌ LOẠI BỎ: Không có trường 'genre' trong Anime Entity]
    // public Map<String, Long> getGenreStatistics() { ... }

    /**
     * Thống kê theo mùa (season)
     * Lấy season name (giả định Season entity có phương thức getName() hoặc toString() hợp lệ)
     */
    public Map<String, Long> getSeasonStatistics() {
        return trackingRepository.findAll().stream()
                .filter(t -> t.getAnime() != null && t.getAnime().getSeason() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getAnime().getSeason().toString(), // Hoặc t.getAnime().getSeason().getName()
                        Collectors.counting()
                ));
    }
}