package com.project.shared;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking;
import com.project.util.AssetUtil;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TrackingScheduleCard extends StackPane  {
    @FXML
    private ImageView posterImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label episodeLabel;
    @FXML
    private Label lastWatchedLabel;
    @FXML
    private Label timeLabel;

    private final TrackingScheduleCardDto dto;

    public TrackingScheduleCard(TrackingScheduleCardDto dto) {
        this.dto = dto;
          URL cssUrl = getClass().getResource("/com/project/css/schedule-card.css");
        if (cssUrl != null) {
            this.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("CSS file: /com/project/css/schedule-card.css not found!");
        }
        this.getStyleClass().add("schedule-card");

        Image posterImage = AssetUtil.getImageFromLocal(dto.getPosterUrl());
        // Poster
        posterImageView = new ImageView(posterImage);
        posterImageView.setFitWidth(250);
        posterImageView.setFitHeight(300);
        posterImageView.setPreserveRatio(false);
        posterImageView.setSmooth(true);
        posterImageView.getStyleClass().add("card-poster");

        // Badges that overlay on top of the poster
        Label titleBadge = new Label(dto.getTitle());
        titleBadge.setWrapText(true);
        titleBadge.getStyleClass().addAll("badge", "badge-title");

        Label timeBadge = new Label(dto.getScheduleLocalTime() == null ? "-" : dto.getScheduleLocalTime().toString());
        timeBadge.getStyleClass().addAll("badge", "badge-time");


        Label lastWatchedBadge = new Label(
                "Last: " + (dto.getLastWatchedEpisode() == null ? "0" : dto.getLastWatchedEpisode()));
        Label episodeBadge = new Label("Ep: " + (dto.getTotalEpisodes() == null ? "?" : dto.getTotalEpisodes()));

        episodeBadge.getStyleClass().addAll("badge", "badge-ep");
        lastWatchedBadge.getStyleClass().addAll("badge", "badge-last");

        HBox badgesOverlay = new HBox(8, timeBadge, episodeBadge, lastWatchedBadge);
        VBox infoVBox = new VBox(4, titleBadge, badgesOverlay);
        infoVBox.getStyleClass().add("badges-overlay");
        infoVBox.setAlignment(Pos.TOP_LEFT);


        this.getChildren().addAll(posterImageView, infoVBox);

        // align overlays
        StackPane.setAlignment(infoVBox, Pos.TOP_LEFT);
        StackPane.setMargin(infoVBox, new Insets(8));
    }

    public TrackingScheduleCardDto getDto() {
        return dto;
    }

}
