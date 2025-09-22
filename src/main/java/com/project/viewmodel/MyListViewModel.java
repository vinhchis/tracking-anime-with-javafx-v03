package com.project.viewmodel;


import java.util.ArrayList;
import java.util.List;

import com.project.dto.TrackingDto;
import com.project.entity.Tracking;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.service.TrackingService;
import com.project.shared.TrackingCard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyListViewModel {
    private ObservableList<TrackingDto> trackingList = FXCollections.observableArrayList();
    private ObservableList<TrackingDto> filteredList = FXCollections.observableArrayList();
    private StringProperty filterStatus = new SimpleStringProperty("All");
    private List<Long> deletedList = new ArrayList<>();
    private final TrackingService trackingService;
    public MyListViewModel() {
        trackingService = new TrackingService();
        // load data from database
        trackingList.setAll(trackingService.getTrackingDtos());
        filteredList.setAll(getTrackingListByStatus(filterStatus.get()));

        // event
        filterStatus.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filteredList.setAll(getTrackingListByStatus(newVal));
            }
        });
    }

    public ObservableList<TrackingDto> getTrackingListByStatus(String status) {
        if (status.equals("All")) {
            return trackingList;
        }

        ObservableList<TrackingDto> filteredList = FXCollections.observableArrayList();
        Tracking.TRACKINGS_STATUS enumStatus = null;
        switch (status) {
            case "Watching":
                enumStatus = TRACKINGS_STATUS.WATCHING;
                break;
            case "On Hold":
                enumStatus = TRACKINGS_STATUS.ON_HOLD;
                break;
            case "Completed":
                enumStatus = TRACKINGS_STATUS.COMPLETED;
                break;
            case "Dropped":
                enumStatus = TRACKINGS_STATUS.DROPPED;
                break;
            case "Plan to Watch":
                enumStatus = TRACKINGS_STATUS.PLAN_TO_WATCH;
                break;
            default:
                break;
        }

        for (TrackingDto dto : trackingList) {
            if (dto.getTrackingStatus().equals(enumStatus)) {
                filteredList.add(dto);
            }
        }

        return filteredList;
    }

    public void save() {
        trackingService.saveAll(trackingList);
        for (Long id : deletedList) {
            trackingService.deleteTrackingById(id);
        }
    }

    public void deleteTrackingById(Long trackingId) {
        // trackingService.deleteTrackingById(trackingId);
        deletedList.add(trackingId);
        trackingList.removeIf(t -> t.getTrackingId() == trackingId);
        filteredList.removeIf(t -> t.getTrackingId() == trackingId);
    }

    public void updateTrackingCardInfo(TrackingCard card) {
        TrackingDto dto = card.getData();
        for (int i = 0; i < trackingList.size(); i++) {
            if (trackingList.get(i).getTrackingId() == dto.getTrackingId()) {
                trackingList.set(i, dto);
                break;
            }
        }

        filteredList.setAll(getTrackingListByStatus(filterStatus.get()));
    }

    // Getters and Setters

     public ObservableList<TrackingDto> getFilteredList() {
        return filteredList;
    }

    public StringProperty filterStatusProperty() {
        return filterStatus;
    }

}
