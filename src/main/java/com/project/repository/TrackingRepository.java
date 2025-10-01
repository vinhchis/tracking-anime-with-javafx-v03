package com.project.repository;

import com.project.entity.Tracking;
import com.project.entity.Tracking.TrackingStatus;
import java.util.List;

/**
 * Repository cho entity Tracking.
 * Bổ sung các query đặc thù ngoài CRUD cơ bản.
 */
// Thay thế extends JpaRepository bằng việc implements các phương thức CRUD
public interface TrackingRepository {

    // Các hàm CRUD mà TrackingRepositoryImpl đã triển khai (copy từ JpaRepository.java nếu cần)
    long countAll();
    List<Tracking> findAll();
    // Thêm các hàm CRUD còn thiếu nếu cần: save(T entity), findById(ID id), delete(T entity), ...

    // Hàm đã có
    List<Tracking> findByStatus(TrackingStatus status);

    // --- Yêu cầu của Loc cho Thống kê (Statistics) ---
    /**
     * Nhóm và đếm số lượng Tracking theo trạng thái (TrackingStatus).
     * Trả về List các Object[]: [TrackingStatus, Long count]
     */
    List<Object[]> countTrackingByStatus();

    // --- Yêu cầu của Loc cho Lịch chiếu (Scheduling) ---
    /**
     * Lấy danh sách Tracking có thiết lập showtime (không null)
     * và được sắp xếp theo showtime.
     */
    List<Tracking> findByShowtimeIsNotNullOrderByShowtimeAsc();
}