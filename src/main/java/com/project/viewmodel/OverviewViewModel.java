package com.project.viewmodel;

import java.util.List;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.service.TrackingService;

import javafx.beans.property.ObjectProperty;
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

    public OverviewViewModel() {
        this.trackingService = new TrackingService();
        allScheduleCardDtos.setAll(trackingService.getScheduleCardDtos());
        // scheduleCardDtos.setAll(trackingService.getScheduleCardDtos());
        scheduleList = new FilteredList<>(allScheduleCardDtos, p -> true);

        selectedDay.addListener((obs, oldDay, newDay) -> {
            if (newDay != null) {
                scheduleList.setPredicate(dto -> dto.getScheduleDay() == newDay);
            } else {
                scheduleList.setPredicate(p -> true); // no filter
            }
        });
    }

    public long getTotalAnimeCount() {
        return trackingService.countAllAnime();
    }

    public long getAnimeCountByStatus(TRACKINGS_STATUS status) {
        return trackingService.getAllTrackings().stream()
                .filter(t -> t.getTrackingStatus() == status)
                .count();
    }

    public void setSelectedDay(ObjectProperty<DAY_OF_WEEK> selectedDay) {
        this.selectedDay = selectedDay;
    }

}
