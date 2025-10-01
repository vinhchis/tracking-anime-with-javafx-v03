// src/main/java/com/project/repository/TrackingRepositoryImpl.java

package com.project.repository;

import com.project.entity.Tracking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class TrackingRepositoryImpl implements TrackingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Hàm đã có (Loc cung cấp)
    @Override
    public long countAll() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Tracking t", Long.class)
                .getSingleResult();
    }

    // Hàm đã có (Loc cung cấp)
    @Override
    public List<Tracking> findByStatus(Tracking.TrackingStatus status) {
        return entityManager.createQuery("SELECT t FROM Tracking t WHERE t.trackingStatus = :status", Tracking.class)
                .setParameter("status", status)
                .getResultList();
    }

    // Hàm đã có (Loc cung cấp)
    @Override
    public List<Tracking> findAll() {
        return entityManager.createQuery("SELECT t FROM Tracking t", Tracking.class)
                .getResultList();
    }

    // --- Bổ sung Logic Thống kê (Statistics) ---
    @Override
    public List<Object[]> countTrackingByStatus() {
        // JPQL: GROUP BY trackingStatus và COUNT số lượng tương ứng
        String jpql = "SELECT t.trackingStatus, COUNT(t) FROM Tracking t GROUP BY t.trackingStatus";
        return entityManager.createQuery(jpql, Object[].class)
                .getResultList();
    }

    // --- Bổ sung Logic Lịch chiếu (Scheduling) ---
    @Override
    public List<Tracking> findByShowtimeIsNotNullOrderByShowtimeAsc() {
        // JPQL: Lấy các bản ghi có showtime khác NULL và sắp xếp theo showtime
        String jpql = "SELECT t FROM Tracking t WHERE t.showtime IS NOT NULL ORDER BY t.showtime ASC";
        return entityManager.createQuery(jpql, Tracking.class)
                .getResultList();
    }
}