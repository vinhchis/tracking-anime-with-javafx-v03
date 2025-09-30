package com.project.shared;

import java.time.LocalTime;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking;
import com.project.util.AssetUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TrackingScheduleCard extends StackPane {
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
        Image posterImage = AssetUtil.getImageFromLocal(dto.getPosterUrl());
        posterImageView = new ImageView(posterImage);
        titleLabel = new Label(dto.getTitle());
        lastWatchedLabel = new Label("Last Watched: " + (dto.getLastWatchedEpisode() == null ? "0" : dto.getLastWatchedEpisode()));
        episodeLabel = new Label("Total Episodes: " + (dto.getTotalEpisodes() == null ? "Unknown" : dto.getTotalEpisodes()));
        timeLabel = new Label("Time: " + dto.getScheduleLocalTime().toString());

        posterImageView.setFitWidth(100);
        posterImageView.setFitHeight(150);
        posterImageView.setPreserveRatio(false);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        lastWatchedLabel.setStyle("-fx-font-size: 12px;");
        episodeLabel.setStyle("-fx-font-size: 12px;");
        timeLabel.setStyle("-fx-font-size: 12px;");
        VBox infoBox = new VBox(5, titleLabel, lastWatchedLabel, episodeLabel, timeLabel);
        infoBox.setStyle("-fx-padding: 10;");
        this.getChildren().addAll(posterImageView, infoBox);
        this.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        this.setPrefWidth(220);
        this.setPrefHeight(160);
        StackPane.setAlignment(posterImageView, javafx.geometry.Pos.CENTER_LEFT);
        StackPane.setAlignment(infoBox, javafx.geometry.Pos.CENTER_RIGHT);
        StackPane.setMargin(infoBox, new javafx.geometry.Insets(0, 10, 0, 0));
        StackPane.setMargin(posterImageView, new javafx.geometry.Insets(0, 0, 0, 10));
    }

    public TrackingScheduleCardDto getDto() {
        return dto;
    }

}
