package com.project.repository;

import com.project.entity.Tracking;
import java.util.List;

/**
 * Repository cho entity Tracking.
 * Kế thừa từ JpaRepository để dùng CRUD cơ bản,
 * và bổ sung các query đặc thù.
 */
public interface TrackingRepository extends JpaRepository<Tracking, Integer> {

    /**
     * Đếm tổng số bản ghi Tracking.
     */
    default long countAll() {
        return 0;
    }

    /**
     * Lấy tất cả bản ghi theo trạng thái.
     *
     * @param status trạng thái cần tìm
     * @return danh sách Tracking có trạng thái tương ứng
     */
    List<Tracking> findByStatus(Tracking.TrackingStatus status);
}