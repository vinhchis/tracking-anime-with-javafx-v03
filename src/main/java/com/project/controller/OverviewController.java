package com.project.controller;

import com.project.navigation.Refreshable;
import com.project.service.StatisticsService;
import com.project.repository.TrackingRepositoryImpl; // giả định có repo này
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Map;

public class OverviewController implements Refreshable {

    @FXML
    private Label totalAnimeLabel;

    @FXML
    private Label completedAnimeLabel;

    @FXML
    private Label watchingAnimeLabel;

    @FXML
    private GridPane genreStatsGrid;

    @FXML
    private GridPane seasonStatsGrid;

    private StatisticsService statisticsService;

    public OverviewController() {
        // ⚠️ Truyền repository vào service
        this.statisticsService = new StatisticsService(new TrackingRepositoryImpl());
    }

    @FXML
    private void initialize() {
        updateStatistics();
    }

    @Override
    public void refresh() {
        updateStatistics();
    }

    private void updateStatistics() {
        // Tổng số anime
        totalAnimeLabel.setText(String.valueOf(statisticsService.getTotalAnimeCount()));

        // Anime đã hoàn thành
        completedAnimeLabel.setText(String.valueOf(statisticsService.getAnimeCountByStatus("COMPLETED")));

        // Anime đang xem
        watchingAnimeLabel.setText(String.valueOf(statisticsService.getAnimeCountByStatus("WATCHING")));

        // Thống kê theo thể loại
        displayGenreStatistics(statisticsService.getGenreStatistics());

        // Thống kê theo mùa
        displaySeasonStatistics(statisticsService.getSeasonStatistics());
    }

    private void displayGenreStatistics(Map<String, Long> genreStats) {
        genreStatsGrid.getChildren().clear();
        genreStatsGrid.setVgap(10);
        genreStatsGrid.setHgap(10);

        int row = 1;
        int col = 0;

        Label header = new Label("Thống kê theo Thể loại");
        header.getStyleClass().add("section-title");
        GridPane.setColumnSpan(header, 2);
        genreStatsGrid.add(header, 0, 0);

        for (Map.Entry<String, Long> entry : genreStats.entrySet()) {
            VBox card = createStatCard(entry.getKey(), entry.getValue());
            genreStatsGrid.add(card, col, row);
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }

    private void displaySeasonStatistics(Map<String, Long> seasonStats) {
        seasonStatsGrid.getChildren().clear();
        seasonStatsGrid.setVgap(10);
        seasonStatsGrid.setHgap(10);

        int row = 1;
        int col = 0;

        Label header = new Label("Thống kê theo Mùa");
        header.getStyleClass().add("section-title");
        GridPane.setColumnSpan(header, 2);
        seasonStatsGrid.add(header, 0, 0);

        for (Map.Entry<String, Long> entry : seasonStats.entrySet()) {
            VBox card = createStatCard(entry.getKey(), entry.getValue());
            seasonStatsGrid.add(card, col, row);
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createStatCard(String title, long value) {
        VBox card = new VBox(5);
        card.getStyleClass().add("stat-card");
        card.setAlignment(Pos.CENTER);

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.getStyleClass().add("stat-value");
        valueLabel.setFont(new Font("System Bold", 20));

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-label");
        titleLabel.setFont(new Font("System", 14));

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    @Override
    public void onFresh() {
        refresh();
    }
}