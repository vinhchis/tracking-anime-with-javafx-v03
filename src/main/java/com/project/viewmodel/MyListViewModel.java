package com.project.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.project.dto.TrackingDto;
import com.project.entity.Season;
import com.project.entity.Tracking;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.service.TrackingService;
import com.project.shared.TrackingCard;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;

public class MyListViewModel {
    private ObservableList<TrackingDto> trackingList = FXCollections.observableArrayList();
    private FilteredList<TrackingDto> filteredList;
    private StringProperty filterStatus = new SimpleStringProperty("");
    private StringProperty searchText = new SimpleStringProperty("");
    private IntegerProperty totalAnimeWithStatus = new SimpleIntegerProperty(0);
    private ObjectProperty<ObservableList<Pair<String, String>>> seasonFilterObjectProperty = new SimpleObjectProperty<>(
            FXCollections.observableArrayList());

    public ObjectProperty<ObservableList<Pair<String, String>>> getSeasonFilterObjectProperty() {
        return seasonFilterObjectProperty;
    }


    private ObjectProperty<Pair<String, String>> selectedSeason = new SimpleObjectProperty(null);


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
                searchText.set("");
                if(selectedSeason.isBound()) {
                    selectedSeason.unbind();
                }
                seasonFilterObjectProperty.setValue(getSeasonFromTracking(newStatus));
                selectedSeason.set(new Pair<>("All", "All"));
                updateFilteredList();
            }
        });

        searchText.addListener((obs, oldText, newText) -> {
            if (newText != null) {
                updateFilteredList();
            }
        });

        selectedSeason.addListener((obs, oldSeason, newSeason) -> {
            if (newSeason != null) {
                System.out.println("Season changed to: " + newSeason.getKey() + "_" + newSeason.getValue());
                updateFilteredList();
            }
        });
    }

    private void updateFilteredList() {
        String status = filterStatus.get();
        String search = searchText.get().toLowerCase().trim();
        Pair<String, String> newSeason = selectedSeason.get();

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

        Predicate<TrackingDto> seasonPredicate = dto -> {
            if (newSeason == null || newSeason.getKey().equals("All")) {
                return true;
            }

            Season.SEASON_NAME seasonEnum = null;
            Short year = 0;
            try {
                switch (newSeason.getKey()) {
                    case "WINTER":
                        seasonEnum = Season.SEASON_NAME.WINTER;
                        break;
                    case "SPRING":
                        seasonEnum = Season.SEASON_NAME.SPRING;
                        break;
                    case "SUMMER":
                        seasonEnum = Season.SEASON_NAME.SUMMER;
                        break;
                    case "FALL":
                        seasonEnum = Season.SEASON_NAME.FALL;
                        break;
                    default:
                        break;
                }
                year = Short.valueOf(newSeason.getValue());
            } catch (Exception e) {
                System.err.println("Error parsing season or year: " + e.getMessage());
            }

            return dto.getSeasonName() != null && dto.getSeasonYear() != null &&
                    dto.getSeasonName().equals(seasonEnum) &&
                    dto.getSeasonYear().equals(year);
        };
        // get total anime before filter
        totalAnimeWithStatus.set(getStatusAnimeCount(enumStatus));

        filteredList.setPredicate(dto -> {
            boolean statusMatches = status.equals("All") || dto.getTrackingStatus().equals(enumStatus);
            boolean searchMatches = search.isEmpty() || dto.getAnimeTitle().toLowerCase().contains(search) ||
                    (dto.getStudioName() != null && dto.getStudioName().toLowerCase().contains(search));
            boolean seasonMatches = seasonPredicate.test(dto);
            return statusMatches && searchMatches && seasonMatches;
        });
    }

    private int getStatusAnimeCount(Tracking.TRACKINGS_STATUS status) {
        if (status == null) {
            return trackingList.size();
        }
        return (int) trackingList.stream().filter(t -> t.getTrackingStatus() == status).count();
    }

    private ObservableList<Pair<String, String>> getSeasonFromTracking(String status) {
        ObservableList<Pair<String, String>> seasons = FXCollections.observableArrayList();
        seasons.add(new Pair<>("All", "All"));
        if (status.equals("All")) {
            for (TrackingDto dto : trackingList) {
                if (dto.getSeasonName() != null && dto.getSeasonYear() != null) {
                    String seasonStr = dto.getSeasonName().toString();
                    String yearStr = dto.getSeasonYear().toString();
                    Pair<String, String> seasonPair = new Pair<>(seasonStr, yearStr);
                    if (!seasons.contains(seasonPair)) {
                        seasons.add(seasonPair);
                    }
                }
            }
            return seasons;

        }

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
        for (TrackingDto dto : trackingList) {
            if (dto.getTrackingStatus() == enumStatus && dto.getSeasonName() != null && dto.getSeasonYear() != null) {
                String seasonStr = dto.getSeasonName().toString();
                String yearStr = dto.getSeasonYear().toString();
                Pair<String, String> seasonPair = new Pair<>(seasonStr, yearStr);
                if (!seasons.contains(seasonPair)) {
                    seasons.add(seasonPair);
                }
            }
        }
        return seasons;
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
     public ObjectProperty<Pair<String, String>> getSelectedSeason() {
        return selectedSeason;
    }

    public IntegerProperty getTotalAnimeWithStatus() {
        return totalAnimeWithStatus;
    }

    public StringProperty getSearchText() {
        return searchText;
    }

}
