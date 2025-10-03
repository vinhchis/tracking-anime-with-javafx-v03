package com.project.viewmodel;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.service.StatisticsService; // Import StatisticsService
import com.project.service.TrackingService;

import javafx.beans.property.LongProperty; // Dùng LongProperty cho số lượng
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class OverviewViewModel {
    private final TrackingService trackingService;
    private final StatisticsService statisticsService; // Khai báo StatisticsService

    // Dùng LongProperty cho thống kê
    private final LongProperty totalTrackingCount = new SimpleLongProperty(0);
    private final LongProperty watchingCount = new SimpleLongProperty(0);
    private final LongProperty completedCount = new SimpleLongProperty(0);
    // Có thể thêm các trạng thái khác nếu cần (ON_HOLD, DROPPED, PLAN_TO_WATCH)

    private final ObservableList<TrackingScheduleCardDto> scheduleCardDtos = FXCollections.observableArrayList();
    private final ObjectProperty<DAY_OF_WEEK> selectedDay = new SimpleObjectProperty<>(null);
    private final FilteredList<TrackingScheduleCardDto> scheduleList;

    public FilteredList<TrackingScheduleCardDto> getScheduleList() {
        return scheduleList;
    }

    public ObjectProperty<DAY_OF_WEEK> getSelectedDay() {
        return selectedDay;
    }

    // --- Getters cho Thống kê ---
    public LongProperty totalTrackingCountProperty() {
        return totalTrackingCount;
    }

    public LongProperty watchingCountProperty() {
        return watchingCount;
    }

    public LongProperty completedCountProperty() {
        return completedCount;
    }


    // Constructor phải nhận cả hai service (hoặc tự khởi tạo nếu không dùng DI framework)
    // Giả định: ViewModel tự khởi tạo Service như cách đã làm trong TrackingService
    public OverviewViewModel() {
        // Khởi tạo các Service
        this.trackingService = new TrackingService();
        // Lộc cần khởi tạo TrackingRepository trước khi khởi tạo StatisticsService
        this.statisticsService = new StatisticsService(this.trackingService.trackingRepository);

        // 1. Tải dữ liệu lịch chiếu (dùng phương thức mới, tối ưu)
        loadScheduleData();

        // 2. Khởi tạo FilteredList sau khi đã có dữ liệu
        scheduleList = new FilteredList<>(scheduleCardDtos, p -> true);

        // 3. Thiết lập Listener cho Filter
        selectedDay.addListener((obs, oldDay, newDay) -> {
            if (newDay != null) {
                scheduleList.setPredicate(dto -> dto.getScheduleDay() == newDay);
            } else {
                scheduleList.setPredicate(p -> true); // no filter
            }
        });

        // 4. Tải dữ liệu thống kê
        loadStatisticsData();
    }

    // Phương thức tải dữ liệu lịch chiếu
    public void loadScheduleData() {
        // Dùng phương thức đã tối ưu của Lộc (Bước 3)
        scheduleCardDtos.setAll(trackingService.getScheduledTrackingCardDtos());
    }

    // Phương thức tải dữ liệu thống kê (Sử dụng StatisticsService của Lộc - Bước 1)
    public void loadStatisticsData() {
        totalTrackingCount.set(statisticsService.getTotalTrackingCount());
        watchingCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.WATCHING));
        completedCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.COMPLETED));
    }


    // LOẠI BỎ CÁC PHƯƠNG THỨC THỐNG KÊ CŨ KHÔNG CẦN THIẾT
    /*
    public long getTotalAnimeCount() { ... }
    public long getAnimeCountByStatus(TRACKINGS_STATUS status) { ... }
    public void setSelectedDay(ObjectProperty<DAY_OF_WEEK> selectedDay) { ... } // Không cần setter cho property
    */

    //
    public StatisticsService getStatisticsService() {
        return statisticsService;
    }

}