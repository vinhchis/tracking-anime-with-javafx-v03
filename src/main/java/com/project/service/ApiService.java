package com.project.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.project.dto.AnimeCardDto;
import com.project.dto.DataSaveDto;
import com.project.dto.JikanAnimeResponse;
import com.project.dto.JikanAnimeResponse.AnimeData;
import com.project.dto.JikanSearchResponse;
import com.project.entity.Anime;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.entity.Tracking;
import com.project.entity.Season.SEASON_NAME;
import com.project.util.MapperUtil;

public class ApiService {
    private final String baseApi = "https://api.jikan.moe/v4";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ApiService() {

    }

    private JikanAnimeResponse.AnimeData getAnimeById(int id) throws Exception {
        String endpoint = baseApi + "/anime/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse response and map to AnimeDto
        JikanAnimeResponse dto = gson.fromJson(response.body(), JikanAnimeResponse.class);
        // Anime anime = MapperUtil.mapToAnime(dto.getData());
        return dto.getData();
    }

    // public CompletableFuture<JikanAnimeResponse> getAnimeByIdAsync(int id) {
    // return CompletableFuture.supplyAsync(() -> {
    // try {
    // return getAnimeById(id);
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // }
    // });
    // }

    // public CompletableFuture<Anime> getAnimeEntityByIdAsync(int id) {
    // return getAnimeByIdAsync(id).thenApply(dto ->
    // MapperUtil.mapToAnime(dto.getData()));
    // }

    private List<JikanAnimeResponse.AnimeData> searchAnime(String query, int limit) throws Exception {
        String endpoint =baseApi + "/anime?q=" + query + "&limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse response and map to List<AnimeDto>
        // Assuming the response structure has a 'data' field containing the list
        JikanSearchResponse searchResponse = gson.fromJson(response.body(), JikanSearchResponse.class);
        return searchResponse.getData();
    }

    private List<JikanAnimeResponse.AnimeData> getTopAnime(int limit) throws Exception {
        String endpoint = baseApi + "/top/anime" + "?limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse response and map to List<AnimeDto>
        JikanSearchResponse topResponse = gson.fromJson(response.body(), JikanSearchResponse.class);
        return topResponse.getData();
    }

    public List<AnimeCardDto> searchAnimeCardDto(String query, int limit) throws Exception {
        List<AnimeData> animeDataList = searchAnime(query, limit);
        return animeDataList.stream()
                .map(this::mapToAnimeCardDto)
                .toList();
    }

    public List<AnimeCardDto> getTopAnimeCardDto(int limit) throws Exception {
        List<AnimeData> animeDataList = getTopAnime(limit);
        return animeDataList.stream()
                .map(this::mapToAnimeCardDto)
                .toList();
    }

    // mapper
    public DataSaveDto mapToDataSave(AnimeCardDto dto) {
        DataSaveDto dataSaveDto = new DataSaveDto();
        Anime anime = new Anime();
        Studio studio = new Studio();
        Season season = new Season();

        // anime
        anime.setApiId(dto.getApiId());
        anime.setTitle(dto.getTitle());
        anime.setPosterUrl(dto.getPosterUrl());
        anime.setIntroduction(dto.getSynopsis());
        anime.setTotalEpisodes(dto.getTotalEpisodes());
       if (dto.getAnimeStatus() != null) {
            switch (dto.getAnimeStatus().toLowerCase()) {
                case "finished airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.COMPLETED);
                    break;
                case "currently airing":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.ONGOING);
                    break;
                case "not yet aired":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.UPCOMING);
                    break;
                case "canceled":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.CANCELLED);
                    break;
                case "hiatus":
                    anime.setAnimeStatus(Anime.ANIME_STATUS.HIATUS);
                    break;
            }
        }

        if (dto.getAnimeType() != null) {
            switch (dto.getAnimeType().toUpperCase()) {
                case "TV":
                    anime.setAnimeType(Anime.ANIME_TYPE.TV);
                    break;
                case "MOVIE":
                    anime.setAnimeType(Anime.ANIME_TYPE.MOVIE);
                    break;
                case "OVA":
                    anime.setAnimeType(Anime.ANIME_TYPE.OVA);
                    break;
                case "SPECIAL":
                    anime.setAnimeType(Anime.ANIME_TYPE.SPECIAL);
                    break;
            }
        }

        // season
        if (dto.getSeasonYear() != null) {
            season.setSeasonYear(dto.getSeasonYear());
        }

        if (dto.getSeasonName() != null) {
            switch (dto.getSeasonName().toLowerCase()) {
                case "winter":
                    season.setSeasonName(Season.SEASON_NAME.WINTER);
                    break;
                case "spring":
                    season.setSeasonName(SEASON_NAME.SPRING);
                    break;
                case "summer":
                    season.setSeasonName(SEASON_NAME.SUMMER);
                    break;
                case "fall":
                    season.setSeasonName(SEASON_NAME.FALL);
                    break;
            }

        }
        studio.setStudioName(dto.getStudioName());

        dataSaveDto.setAnime(anime);
        dataSaveDto.setStudio(studio);
        dataSaveDto.setSeason(season);
        return dataSaveDto;
    }


    public AnimeCardDto mapToAnimeCardDto(AnimeData dto) {
        AnimeCardDto animeCardDto = new AnimeCardDto();

        animeCardDto.setApiId(Integer.valueOf(dto.getMal_id()));
        animeCardDto.setTitle(dto.getTitle());
        animeCardDto.setSynopsis(dto.getSynopsis());
        animeCardDto.setScore(dto.getScore());
        animeCardDto.setUrl(dto.getUrl());
        animeCardDto.setPosterUrl(dto.getImages().getJpg().getImage_url());
        animeCardDto.setTotalEpisodes(dto.getEpisodes() != null ? Short.valueOf(dto.getEpisodes().toString()) : null);

        animeCardDto.setAnimeStatus(dto.getStatus());
        animeCardDto.setAnimeType(dto.getType());
        if (dto.getYear() != null) {
            animeCardDto.setSeasonYear(Short.valueOf(dto.getYear().toString()));
        }
        animeCardDto.setSeasonName(dto.getSeason());

        if (dto.getStudios() != null && !dto.getStudios().isEmpty()) {
            animeCardDto.setStudioName(String.valueOf(dto.getStudios().get(0).getName()));
        }

        return animeCardDto;

    }
}