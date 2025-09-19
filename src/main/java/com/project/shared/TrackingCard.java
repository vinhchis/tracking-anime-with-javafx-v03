package com.project.shared;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.Rating;

import com.project.entity.Tracking.TRACKINGS_STATUS;

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

    @FXML
    private Label seasonNameLabel; // season year + name
    @FXML
    private Label seasonYearLabel;

    @FXML
    private Label studioLabel;

    // tracking info
    @FXML
    private Label currentEpisodeLabel;
    @FXML
    private Rating rating;
    @FXML
    private ComboBox<TRACKINGS_STATUS> trackingStatusComboBox;
    @FXML
    private ComboBox<String> scheduleDayComboBox;
    @FXML
    private TimePicker scheduleTimePicker;
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

    public TrackingCard() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/shared/TrackingCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TrackingCard FXML", e);
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        scheduleTimePicker.timeProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Chọn giờ: " + newVal);
        });
    }

}
