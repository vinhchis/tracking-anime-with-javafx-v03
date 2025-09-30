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

public class OverviewViewModel {
    private TrackingService trackingService;
    private ObjectProperty<DAY_OF_WEEK> selectedDay = new SimpleObjectProperty<>(null);
    public void setSelectedDay(ObjectProperty<DAY_OF_WEEK> selectedDay) {
        this.selectedDay = selectedDay;
    }


    public ObjectProperty<DAY_OF_WEEK> getSelectedDay() {
        return selectedDay;
    }

    private ObservableList<TrackingScheduleCardDto> dayList;

    public ObservableList<TrackingScheduleCardDto> getDayList() {
        return dayList;
    }


    public OverviewViewModel() {
        this.trackingService = new TrackingService();
        dayList = FXCollections.observableArrayList();
        List<TrackingScheduleCardDto> allDtos = trackingService.getScheduleCardDtos();

        selectedDay.addListener((obs, oldDay, newDay) -> {
            if (newDay != null) {
                dayList.clear();
                dayList.addAll(allDtos.stream()
                        .filter(dto -> dto.getScheduleDay() == newDay)
                        .toList());
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

}
