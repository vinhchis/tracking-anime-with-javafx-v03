package com.project.shared;

import com.project.dto.AnimeCardDto;
import com.project.util.AssetUtil;
import com.project.util.HoverAnimation;

import java.net.URL;

import org.controlsfx.control.spreadsheet.Grid;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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

    }

    private void showDetailsDialog(AnimeCardDto dto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ANIME DETAILS");
        alert.setWidth(800);
        alert.setResizable(true);
        alert.setHeaderText(dto.getTitle() + " Details");

        GridPane grid = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(100);
        col1.setPrefWidth(120);
        col1.setMaxWidth(150);
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        grid.getColumnConstraints().addAll(col1, col2);

        // grid.addRow(0, new Label("Anime ID:"), new Label(String.valueOf(dto.getAnimeId())));
        // grid.addRow(1, new Label("API ID:"), new Label(String.valueOf(dto.getApiId())));
        grid.addRow(0, new Label("Title:"), new Label(dto.getTitle()));
        TextArea synopsisArea = new TextArea(dto.getSynopsis());
        synopsisArea.setWrapText(true);
        synopsisArea.setEditable(false);
        synopsisArea.setPrefRowCount(5);
        synopsisArea.setPrefWidth(400);
        synopsisArea.setMaxWidth(600);
        synopsisArea.setMaxHeight(400);
        grid.addRow(1, new Label("Synopsis:"), synopsisArea);
        grid.addRow(2, new Label("Type:"), new Label(dto.getAnimeType()));
        grid.addRow(3, new Label("Status:"), new Label(dto.getAnimeStatus()));
        grid.addRow(4, new Label("Episodes:"), new Label(String.valueOf(dto.getTotalEpisodes() != null ? dto.getTotalEpisodes() : "N/A")));
        grid.addRow(5, new Label("Score:"), new Label(String.valueOf(dto.getScore()) + "/10"));
        grid.addRow(6, new Label("Studio:"), new Label(dto.getStudioName()));
        grid.addRow(7, new Label("Season:"), new Label(
            (dto.getSeasonName() != null ? dto.getSeasonName() : "N/A") + " " +
            (dto.getSeasonYear()) != null ? dto.getSeasonYear().toString() : "N/A"));
        grid.addRow(8, new Label("URL:"), new Hyperlink(dto.getUrl()));

        alert.getDialogPane().setContent(grid);
        alert.showAndWait();
    }

    public Button getAddBtn() {
        return addBtn;
    }
}
