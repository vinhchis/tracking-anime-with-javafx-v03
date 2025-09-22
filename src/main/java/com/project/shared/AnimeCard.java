package com.project.shared;

import com.project.dto.AnimeCardDto;
import com.project.util.AssetUtil;
import com.project.util.HoverAnimation;

import java.net.URL;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

public class AnimeCard extends VBox {
    private final Label titleLabel;
    private final Label statusLabel;
    private final Label typeLabel;
    private final Label epsLabel;
    private final Label scoreLabel;

    private final Label studioLabel;
    private final Label seasonNameLabel;
    private final Label seasonYearLabel;


    private final Button addBtn;
    private final Button detailBtn;

    private final AnimeCardDto dto;


    public AnimeCard(AnimeCardDto dto) {
        // ===== Card style =====
        this.setPrefWidth(250);
        URL cssUrl = getClass().getResource("/com/project/css/anime-card.css");
        if (cssUrl != null) {
            this.getStylesheets().add(cssUrl.toExternalForm());
        }
        // assign the card class
        this.getStyleClass().add("anime-card");
        this.dto = dto;


        // ===== Image section =====
        StackPane imagePane = new StackPane();
        Image posterImage = AssetUtil.getImageFromLink(dto.getPosterUrl());

        ImageView poster = new ImageView(posterImage);
        poster.setFitWidth(250);
        poster.setFitHeight(300);
        poster.setPreserveRatio(false);

        // Badges
        typeLabel = new Label(dto.getAnimeStatus());
        typeLabel.getStyleClass().add("badge");

        statusLabel = new Label(dto.getAnimeType());
        statusLabel.getStyleClass().add("badge");

        HBox badges = new HBox(5, typeLabel, statusLabel);
        badges.setAlignment(Pos.TOP_LEFT);
        badges.setPadding(new Insets(10));

        // Button addBtnTop = new Button("+");
        // addBtnTop.getStyleClass().add("add-top-btn");
        // StackPane.setAlignment(addBtnTop, Pos.TOP_RIGHT);
        // StackPane.setMargin(addBtnTop, new Insets(10));

        // imagePane.getChildren().addAll(poster, badges, addBtnTop);
        imagePane.getChildren().addAll(poster, badges);


        // ===== Info section =====
        VBox infoBox = new VBox(5);
        infoBox.setPadding(new Insets(10));

        titleLabel = new Label(dto.getTitle());
        titleLabel.getStyleClass().add("title");

        studioLabel = new Label("🎬 " + (dto.getStudioName() != null ? dto.getStudioName() : "N/A"));
        studioLabel.getStyleClass().add("studio-label");

        seasonNameLabel = new Label("🍂 " + (dto.getSeasonName() != null ? dto.getSeasonName() : "N/A"));
        seasonNameLabel.getStyleClass().add("season-name-label");

        seasonYearLabel = new Label("📅 " + (dto.getSeasonYear() != null ? dto.getSeasonYear().toString() : "N/A"));
        seasonYearLabel.getStyleClass().add("season-year-label");

        scoreLabel = new Label("⭐ " + (dto.getScore() != null ? dto.getScore().toString() : "N/A"));
        scoreLabel.getStyleClass().add("score");

        epsLabel = new Label("📺 " + (dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : "N/A") + " eps");
        epsLabel.getStyleClass().add("eps");

        infoBox.getChildren().addAll(titleLabel, studioLabel, seasonNameLabel, seasonYearLabel, scoreLabel, epsLabel);

        // ===== Add + Details buttons bottom =====
        addBtn = new Button("➕ Add to My List");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.getStyleClass().add("add-btn");

        detailBtn = new Button("ⓘ");
        detailBtn.getStyleClass().add("detail-btn");

        // layout: addBtn grows to take available space
        HBox bottomBtns = new HBox(8, addBtn, detailBtn);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        addBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(bottomBtns, new Insets(0, 10, 10, 10));

        // ===== Combine =====
        this.getChildren().addAll(imagePane, infoBox, bottomBtns);

        // wire detail dialog
        detailBtn.setOnAction(evt -> showDetailsDialog(this.dto));

        // Animation
        // HoverAnimation.install(this);
    }

    private void showDetailsDialog(AnimeCardDto dto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Anime details");
        alert.setHeaderText(dto.getTitle());

        StringBuilder sb = new StringBuilder();
        sb.append("Anime ID: ").append(dto.getAnimeId()).append("\n");
        sb.append("API ID: ").append(dto.getApiId()).append("\n\n");
        sb.append("Title: ").append(dto.getTitle()).append("\n");
        sb.append("Type: ").append(dto.getAnimeType()).append("\n");
        sb.append("Status: ").append(dto.getAnimeStatus()).append("\n");
        sb.append("Episodes: ").append(dto.getTotalEpisodes()).append("\n");
        sb.append("Score: ").append(dto.getScore()).append("\n");
        sb.append("Studio: ").append(dto.getStudioName()).append("\n");
        sb.append("Season: ").append(dto.getSeasonName()).append(" ").append(dto.getSeasonYear()).append("\n");
        sb.append("URL: ").append(dto.getUrl()).append("\n\n");
        sb.append("Synopsis:\n").append(dto.getSynopsis());

        TextArea area = new TextArea(sb.toString());
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefWidth(480);
        area.setPrefHeight(320);

        alert.getDialogPane().setExpandableContent(area);
        alert.getDialogPane().setExpanded(true);
        alert.showAndWait();
    }

    public Button getAddBtn() {
        return addBtn;
    }
}


