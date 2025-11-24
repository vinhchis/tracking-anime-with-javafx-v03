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
    private ObservableList<TrackingScheduleCardDto> allScheduleCardDtos = FXCollections.observableArrayList();
    private ObservableList<TrackingScheduleCardDto> scheduleCardDtos = FXCollections.observableArrayList();
    private ObjectProperty<DAY_OF_WEEK> selectedDay = new SimpleObjectProperty<>(null);
    private FilteredList<TrackingScheduleCardDto> scheduleList;

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

    // thêm lần 1: getters cho 3 property mới
    public LongProperty onHoldCountProperty() {
        return onHoldCount;
    }

    public LongProperty droppedCountProperty() {
        return droppedCount;
    }

    public LongProperty planToWatchCountProperty() {
        return planToWatchCount;
    }

    // Constructor phải nhận cả hai service (hoặc tự khởi tạo nếu không dùng DI framework)
    public OverviewViewModel() {
        // Khởi tạo các Service
        this.trackingService = new TrackingService();
        allScheduleCardDtos.setAll(trackingService.getScheduleCardDtos());
        // scheduleCardDtos.setAll(trackingService.getScheduleCardDtos());
        scheduleList = new FilteredList<>(allScheduleCardDtos, p -> true);

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
        scheduleCardDtos.setAll(trackingService.getScheduledTrackingCardDtos());
    }

    // Phương thức tải dữ liệu thống kê
    public void loadStatisticsData() {
        totalTrackingCount.set(statisticsService.getTotalTrackingCount());
        watchingCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.WATCHING));
        completedCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.COMPLETED));

        // thêm lần 1: gán giá trị cho các trạng thái mới
        onHoldCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.ON_HOLD));
        droppedCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.DROPPED));
        planToWatchCount.set(statisticsService.getTrackingCountByStatus(TRACKINGS_STATUS.PLAN_TO_WATCH));
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }

}
