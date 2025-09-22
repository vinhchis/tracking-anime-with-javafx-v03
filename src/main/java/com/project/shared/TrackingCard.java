package com.project.shared;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import org.controlsfx.control.Rating;

import com.project.dto.TrackingDto;
import com.project.entity.Tracking;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.util.AssetUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class TrackingCard extends VBox implements Initializable {
    // anime info
    @FXML
    private ImageView posterImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label animeTypeLabel;
    @FXML
    private Label animeStatusLabel;
    @FXML
    private Label totalEpisodesLabel;
    // season
    @FXML
    private Label seasonNameLabel; // season year + name
    @FXML
    private Label seasonYearLabel;
    // studio
    @FXML
    private Label studioLabel;

    // tracking info
    @FXML
    private Rating rating;
    @FXML
    private ComboBox<TRACKINGS_STATUS> trackingStatusComboBox;
    @FXML
    private VBox scheduleBox;
    @FXML
    private ComboBox<DAY_OF_WEEK> scheduleDayComboBox;
    @FXML
    private TextArea noteTextArea;

    // action controls
    @FXML
    private Label episodeProgressLabel; // last watched episode / total episodes
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ToggleButton noteToggleButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button increaseBtn;
    @FXML
    private Button decreaseBtn;

    private TrackingDto dto;
    private CustomTimePicker timePicker = new CustomTimePicker();

    public CustomTimePicker getTimePicker() {
        return timePicker;
    }

    public TrackingCard() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/shared/TrackingCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TrackingCard FXML", e);
        }
        this.getStylesheets().add(AssetUtil.getCss("tracking-card.css"));
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        scheduleBox.getChildren().add(timePicker);

        trackingStatusComboBox.getItems().setAll(TRACKINGS_STATUS.values());
        scheduleDayComboBox.getItems().setAll(Tracking.DAY_OF_WEEK.values());
        trackingStatusComboBox.setValue(TRACKINGS_STATUS.WATCHING);
        noteTextArea.setVisible(false);

        noteToggleButton.setOnAction(e -> {
            noteTextArea.setVisible(noteToggleButton.isSelected());
        });
    }

    public void setData(TrackingDto trackingDto) {
        this.dto = trackingDto;

        // Set tracking
        // don't check null because it's already handled in dto
        rating.setRating(dto.getRating());
        trackingStatusComboBox.setValue(dto.getTrackingStatus());
        scheduleDayComboBox.setValue(dto.getScheduleDay());
        timePicker.setValue(dto.getScheduleLocalTime());
        noteTextArea.setText(dto.getNote());
        // Set progress
        short totalEpisodes = dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : 0;
        short lastWatched = dto.getLastWatchedEpisode() != null ? dto.getLastWatchedEpisode() : 0;

        episodeProgressLabel.setText(lastWatched + " / " + totalEpisodes);
        if (totalEpisodes > 0) {
            progressBar.setProgress((double) lastWatched / totalEpisodes);
        } else {
            progressBar.setProgress(0);
        }

        // Set anime info
        titleLabel.setText(dto.getAnimeTitle());
        Image posterImage = AssetUtil.getImageFromProject(dto.getImageUrl());
        posterImageView.setImage(posterImage);
        animeTypeLabel.setText(dto.getAnimeType() != null ? dto.getAnimeType().toString() : "N/A");
        animeStatusLabel.setText(dto.getAnimeStatus() != null ? dto.getAnimeStatus().toString() : "N/A");
        totalEpisodesLabel.setText(String.valueOf(dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : "N/A"));

        // season
        seasonNameLabel.setText(dto.getSeasonName() != null ? dto.getSeasonName().toString().toLowerCase() : "N/A");
        seasonYearLabel
                .setText(dto.getSeasonYear() != null ? String.valueOf(dto.getSeasonYear())  : "N/A");
        // studio
        studioLabel.setText(dto.getStudioName() != null ? dto.getStudioName() : "N/A");
    }

    public void increaseEpisode() {
        short lastWatched = dto.getLastWatchedEpisode() != null ? dto.getLastWatchedEpisode() : 0;
        short totalEpisodes = dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : 0;
        if (totalEpisodes == 0 || lastWatched < totalEpisodes) {
            lastWatched++;
            dto.setLastWatchedEpisode(lastWatched);
            episodeProgressLabel.setText(lastWatched + " / " + (totalEpisodes == 0 ? "Unknown" : totalEpisodes));
            if (totalEpisodes > 0) {
                progressBar.setProgress((double) lastWatched / totalEpisodes);
            } else {
                progressBar.setProgress(0);
            }
        }
    }

    public void decreaseEpisode() {
        short lastWatched = dto.getLastWatchedEpisode() != null ? dto.getLastWatchedEpisode() : 0;
        if (lastWatched > 0) {
            lastWatched--;
            dto.setLastWatchedEpisode(lastWatched);
            short totalEpisodes = dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : 0;
            episodeProgressLabel.setText(lastWatched + " / " + (totalEpisodes == 0 ? "Unknown" : totalEpisodes));
            if (totalEpisodes > 0) {
                progressBar.setProgress((double) lastWatched / totalEpisodes);
            } else {
                progressBar.setProgress(0);
            }
        }
    }

    // Getters and Setters
    public TrackingDto getData() {
        // Update dto with current UI values
        dto.setRating((byte) rating.getRating());
        dto.setTrackingStatus(trackingStatusComboBox.getValue());
        dto.setScheduleDay(scheduleDayComboBox.getValue());
        dto.setScheduleLocalTime(timePicker.getValue());
        dto.setNote(noteTextArea.getText());
        return dto;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getIncreaseBtn() {
        return increaseBtn;
    }

    public Button getDecreaseBtn() {
        return decreaseBtn;
    }

    public ComboBox<TRACKINGS_STATUS> getTrackingStatusComboBox() {
        return trackingStatusComboBox;
    }

    public ComboBox<DAY_OF_WEEK> getScheduleDayComboBox() {
        return scheduleDayComboBox;
    }

    public TextArea getNoteTextArea() {
        return noteTextArea;
    }

    public Rating getRating() {
        return rating;
    }

}
