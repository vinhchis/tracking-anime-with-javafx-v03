package com.project.viewmodel;

import java.util.ArrayList;
import java.util.List;

import com.project.dto.TrackingDto;
import com.project.entity.Tracking;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.service.TrackingService;
import com.project.shared.TrackingCard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class MyListViewModel {
    private ObservableList<TrackingDto> trackingList = FXCollections.observableArrayList();
    private FilteredList<TrackingDto> filteredList;
    private StringProperty filterStatus = new SimpleStringProperty("");
    private StringProperty searchText = new SimpleStringProperty("");
    private IntegerProperty totalAnimeWithStatus = new SimpleIntegerProperty(0);
    public IntegerProperty getTotalAnimeWithStatus() {
        return totalAnimeWithStatus;
    }

    public StringProperty getSearchText() {
        return searchText;
    }

    private List<Long> deletedList = new ArrayList<>();
    private final TrackingService trackingService;

    public MyListViewModel() {
        trackingService = new TrackingService();
        // load data from database
        trackingList.setAll(trackingService.getTrackingDtos());
        filteredList = new FilteredList<>(trackingList, p -> true);
        // event
        filterStatus.addListener((obs, oldStatus, newStatus) -> {
            if (newStatus != null) {
                System.out.println("Filter changed to: " + newStatus);
                updateFilteredList();
                totalAnimeWithStatus.set(filteredList.size());
            }
        });

        searchText.addListener((obs, oldText, newText) -> {
            if (newText != null) {
                updateFilteredList();
            }
        });
    }

    private void updateFilteredList() {
        String status = filterStatus.get();
        String search = searchText.get().toLowerCase().trim();

        final Tracking.TRACKINGS_STATUS enumStatus;
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
                enumStatus = null;
                break;
        }

        filteredList.setPredicate(dto ->
        {
            boolean statusMatches = status.equals("All") || dto.getTrackingStatus().equals(enumStatus);
            boolean searchMatches = search.isEmpty() || dto.getAnimeTitle().toLowerCase().contains(search) ||
                    (dto.getStudioName() != null && dto.getStudioName().toLowerCase().contains(search));
            return statusMatches && searchMatches;
        });

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
    }

    // Getters and Setters
    public ObservableList<TrackingDto> getFilteredList() {
        return filteredList;
    }

    public StringProperty filterStatusProperty() {
        return filterStatus;
    }

}
