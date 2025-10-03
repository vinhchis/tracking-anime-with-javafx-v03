package com.project.service;

import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.repository.TrackingRepository;
import com.project.util.JpaUtil; // Import JpaUtil
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Service chịu trách nhiệm xử lý các nghiệp vụ liên quan đến thống kê
 * dữ liệu Tracking của người dùng.
 */
public class StatisticsService {

    // TrackingRepository vẫn cần thiết cho các nghiệp vụ khác nếu có
    private final TrackingRepository trackingRepository;

    public StatisticsService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    // Phương thức tiện ích để lấy EntityManager mới
    private EntityManager getEntityManager() {
        return JpaUtil.getEntityManagerFactory().createEntityManager();
    }

    /**
     * Lấy tổng số lượng Anime đang được Tracking.
     *
     * @return Tổng số lượng Tracking.
     */
    public Long getTotalTrackingCount() {
        EntityManager em = getEntityManager();
        try {
            // JPQL để đếm tất cả các bản ghi Tracking
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Tracking t", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error getting total tracking count: " + e.getMessage());
            return 0L;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Lấy số lượng Anime đang được Tracking theo trạng thái cụ thể (WATCHING, COMPLETED...).
     *
     * @param status Trạng thái tracking cần đếm.
     * @return Số lượng Tracking với trạng thái đó.
     */
    public Long getTrackingCountByStatus(TRACKINGS_STATUS status) {
        EntityManager em = getEntityManager();
        try {
            // JPQL để đếm các bản ghi Tracking theo trạng thái
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Tracking t WHERE t.trackingStatus = :status",
                    Long.class
            );
            query.setParameter("status", status);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error getting tracking count by status: " + e.getMessage());
            return 0L;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}