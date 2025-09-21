package com.project.controller;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

import com.project.dto.AnimeCardDto;
import com.project.dto.DataSaveDto;
import com.project.service.AnimeService;
import com.project.service.ApiService;
import com.project.shared.AnimeCard;
import com.project.util.AlertUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class DiscoverController implements Initializable {
    @FXML
    private BorderPane myListBorderPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private FlowPane animeFlowPane;

    private final ApiService apiService;

    public DiscoverController() {
        apiService = new ApiService();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        searchButton.setOnAction(event -> {
            String query = searchTextField.getText().trim();
            if (!query.isEmpty()) {
                fetchAndShowAnimeWithQuery(query, 10);
            } else {
                AlertUtil.showAlert(AlertType.WARNING, myListBorderPane.getScene().getWindow(),
                        "Input Required", "Please enter a search term.");
            }
        });
    }

    // Helper: fetch anime off the FX thread and show result on FX thread
    private void fetchAndShowAnimeWithQuery(String q, int limit) {
        CompletableFuture.supplyAsync(() -> {
            try {
                // encode the query to avoid spaces/special-char issues in URLs
                String encoded = URLEncoder.encode(q, StandardCharsets.UTF_8);
                return apiService.searchAnimeCardDto(encoded, limit);
            } catch (Exception e) {
                // wrap checked exception to propagate to exceptionally block
                throw new RuntimeException(e);
            }
        }).thenAccept(list -> {
            Platform.runLater(() -> {
                animeFlowPane.getChildren().clear();
                list.forEach(animeCardDto -> {
                    AnimeCard card = new AnimeCard(animeCardDto);
                    card.getAddBtn().setOnAction(e -> addToMyList(animeCardDto));
                    animeFlowPane.getChildren().add(card);
                });
                String title = "Fetched animes with query: " + q;
                String content = "Successfully fetched " + list.size() + " top anime.";
                AlertUtil.showAlert(AlertType.INFORMATION, myListBorderPane.getScene().getWindow(), title, content);
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                AlertUtil.showAlert(AlertType.ERROR, myListBorderPane.getScene().getWindow(),
                        "Error fetching anime", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
            });
            return null;
        });
    }


    public void addToMyList(AnimeCardDto dto) {
        DataSaveDto saveDto = apiService.mapToDataSave(dto);
        System.err.println("Add to my list: " + saveDto.getAnime().getTitle());

        AlertUtil.showAlert(AlertType.INFORMATION, myListBorderPane.getScene().getWindow(),
            "Added a anime to Your List", "Successfully added " + dto.getTitle() + " to your list.");
    }


}
