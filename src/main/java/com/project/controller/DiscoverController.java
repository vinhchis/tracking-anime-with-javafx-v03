package com.project.controller;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

import com.project.dto.AnimeCardDto;
import com.project.dto.DataSaveDto;
import com.project.entity.Anime;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.entity.Tracking;
import com.project.service.AnimeService;
import com.project.service.ApiService;
import com.project.service.SeasonService;
import com.project.service.StudioService;
import com.project.service.TrackingService;
import com.project.shared.AnimeCard;
import com.project.util.AlertUtil;
import com.project.util.AssetUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class DiscoverController implements Initializable {
    @FXML
    private BorderPane myListBorderPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Tooltip policyTooltip;

    @FXML
    private Button addNewButton;

    @FXML
    private FlowPane animeFlowPane;

    // todo: filter
    @FXML
    private ComboBox<String> filterComboBox;

    private final ApiService apiService;
    private final AnimeService animeService;
    private final SeasonService seasonService;
    private final StudioService studioService;
    private final TrackingService trackingService;

    public DiscoverController() {
        apiService = new ApiService();
        trackingService = new TrackingService();
        animeService = new AnimeService();
        seasonService = new SeasonService();
        studioService = new StudioService();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        policyTooltip.setText(
                "Data provided by Jikan API (https://jikan.moe/)\n"
                        + "Only 10 animes are fetched per search\n"
                        + "After adding to My List, the anime data is saved to local database\n"
                        + "Your Status Tracking for adding anime is set to 'Watching' by default\n"
                        + "Your Schedule Day is set to 'Sunday' and Schedule Time is 8:00 PM by default\n"
                        + "This product is not endorsed by or affiliated with MyAnimeList or its parent company DeNA Co., Ltd.");

        searchButton.setOnAction(event -> {
            String query = searchTextField.getText().trim();
            if (!query.isEmpty()) {
                fetchAndShowAnimeWithQuery(query, 10);
            } else {
                AlertUtil.showAlert(AlertType.WARNING, myListBorderPane.getScene().getWindow(),
                        "Input Required", "Please enter a search term.");
            }
        });

        // todo
        addNewButton.setOnAction(event -> {
            System.out.println("Add new anime button clicked");
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

        // check if anime already in tracking
        if (trackingService.checkExistedAnimeOfTracking(dto.getTitle())) {
            System.out.println("Anime already in tracking: " + dto.getTitle());
            AlertUtil.showAlert(
                    AlertType.ERROR,
                    myListBorderPane.getScene().getWindow(),
                    "Already in Your List",
                    dto.getTitle() + " is already in your list.");
            return;
        }

        Tracking tracking = new Tracking();
        // setting default tracking values // to be made in TrackingDto setter
        // tracking.setTrackingStatus(Tracking.TRACKINGS_STATUS.WATCHING);
        // tracking.setLastWatchedEpisode((short) 0);
        // tracking.setScheduleDay(Tracking.DAY_OF_WEEK.SUNDAY);
        // tracking.setScheduleTime(LocalTime.of(20, 0)); // default 8 PM
        // tracking.setRating(Byte.valueOf("5"));
        // tracking.setNote("Enter your note here...");

        Anime anime = saveDto.getAnime();
        // save file and update posterUrl
        String posterUrl = saveDto.getAnime().getPosterUrl();
        posterUrl = AssetUtil.saveImageToProject(posterUrl);
        anime.setPosterUrl(posterUrl);
        // save studio, season
        if (saveDto.getStudio() != null && saveDto.getStudio().getStudioName() != null) {
            Studio studio = studioService.getStudioByName(saveDto.getStudio().getStudioName());
            if (studio == null) {
                studio = studioService.saveStudio(saveDto.getStudio());
            }

            anime.setStudio(studio);
        }

        // add anime
        if (saveDto.getSeason() != null && saveDto.getSeason().getSeasonName() != null
                && saveDto.getSeason().getSeasonYear() != null) {
            Season season = seasonService.getSeasonByName(saveDto.getSeason().getSeasonName(),
                    saveDto.getSeason().getSeasonYear());
            if (season == null) {
                season = seasonService.saveSeason(saveDto.getSeason());
            }
            anime.setSeason(season);
        }

        anime = animeService.saveAnime(anime);
        // save anime
        tracking.setAnime(animeService.saveAnime(anime));

        // save tracking
        trackingService.saveTracking(tracking);

        System.out.println("Add to my list: " + saveDto.getAnime().getTitle());
        AlertUtil.showAlert(AlertType.INFORMATION, myListBorderPane.getScene().getWindow(),
                "Added a anime to Your List", "Successfully added " + dto.getTitle() + " to your list.");
    }

}
