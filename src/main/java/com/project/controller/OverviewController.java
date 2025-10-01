// src/main/java/com/project/controller/OverviewController.java

package com.project.controller;

import com.project.entity.Tracking.TrackingStatus;
import com.project.navigation.Refreshable;
import com.project.service.StatisticsService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Map;

// Giả định rằng Controller này được tạo bởi một Factory có thể inject Service
public class OverviewController implements Refreshable {

    // --- FXML Components ---
    @FXML
    private Label totalAnimeLabel;

    @FXML
    private Label completedAnimeLabel;

    @FXML
    private Label watchingAnimeLabel;

    @FXML
    private GridPane genreStatsGrid; // Giữ lại cho thống kê Season

    @FXML
    private GridPane seasonStatsGrid;

    // --- Service Injection ---
    // Sử dụng Setter Injection (vì constructor injection phức tạp hơn với JavaFX)
    private StatisticsService statisticsService;

    // Phương thức Setter để hệ thống DI (ví dụ: Spring) có thể inject StatisticsService vào
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @FXML
    private void initialize() {
        // Kiểm tra service đã được inject chưa trước khi dùng (quan trọng)
        if (statisticsService != null) {
            updateStatistics();
        }
    }

    @Override
    public void refresh() {
        // Chỉ refresh nếu service đã sẵn sàng
        if (statisticsService != null) {
            updateStatistics();
        }
    }

    private void updateStatistics() {
        // Lấy dữ liệu thống kê theo trạng thái TỐI ƯU
        Map<TrackingStatus, Long> statusStats = statisticsService.countByStatus();

        // Tổng số anime (sử dụng hàm tối ưu)
        totalAnimeLabel.setText(String.valueOf(statisticsService.countAllTrackedAnime()));

        // Anime đã hoàn thành (Lấy từ Map)
        long completedCount = statusStats.getOrDefault(TrackingStatus.COMPLETED, 0L);
        completedAnimeLabel.setText(String.valueOf(completedCount));

        // Anime đang xem (Lấy từ Map)
        long watchingCount = statusStats.getOrDefault(TrackingStatus.WATCHING, 0L);
        watchingAnimeLabel.setText(String.valueOf(watchingCount));

        // Thống kê theo mùa (season)
        displaySeasonStatistics(statisticsService.getSeasonStatistics());

        // Loại bỏ Thống kê theo Thể loại vì Entity Anime không có trường genre.
        // Xóa grid nếu không dùng
        // ((VBox) genreStatsGrid.getParent()).getChildren().remove(genreStatsGrid);
        // Thay vào đó, ta sẽ dùng genreStatsGrid để hiển thị một nội dung thay thế
        displayGenreStatistics(null);
    }

    // Tận dụng genreStatsGrid để hiển thị thông báo không có dữ liệu thể loại
    private void displayGenreStatistics(Map<String, Long> genreStats) {
        genreStatsGrid.getChildren().clear();

        Label header = new Label("Thống kê theo Thể loại");
        header.getStyleClass().add("section-title");
        GridPane.setColumnSpan(header, 2);
        genreStatsGrid.add(header, 0, 0);

        Label noData = new Label("⚠️ Không có trường 'genre' trong Entity Anime");
        noData.getStyleClass().add("stat-label");
        GridPane.setColumnSpan(noData, 2);
        genreStatsGrid.add(noData, 0, 1);

        // Bạn có thể xóa toàn bộ logic này nếu không cần hiển thị Genre
    }

    private void displaySeasonStatistics(Map<String, Long> seasonStats) {
        seasonStatsGrid.getChildren().clear();
        seasonStatsGrid.setVgap(10);
        seasonStatsGrid.setHgap(10);

        // Bỏ Label header ở đây vì nó đã có trong FXML
        int row = 1;
        int col = 0;

        for (Map.Entry<String, Long> entry : seasonStats.entrySet()) {
            VBox card = createStatCard(entry.getKey(), entry.getValue());
            seasonStatsGrid.add(card, col, row);
            col++;
            if (col > 1) { // 2 cột mỗi hàng
                col = 0;
                row++;
            }
        }
    }

    private VBox createStatCard(String title, long value) {
        // ... (Giữ nguyên hàm này)
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
    //
}