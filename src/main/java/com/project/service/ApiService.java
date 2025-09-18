package com.project.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.project.dto.JikanAnimeResponse;
import com.project.entity.Anime;
import com.project.util.MapperUtil;

public class ApiService {
    private final String baseApi = "https://api.jikan.moe/v4";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ApiService() {

    }

    public Anime getAnimeById(int id) throws Exception {
        String endpoint = baseApi + "/anime/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Parse response and map to AnimeDto
        JikanAnimeResponse dto = gson.fromJson(response.body(), JikanAnimeResponse.class);
        Anime anime = MapperUtil.mapToAnime(dto.getData());
        return anime;
    }

    // public List<Anime> searchAnime(String query) throws Exception {
    //     String endpoint = baseApi + "/anime?q=" + query;
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create(endpoint))
    //             .header("Content-Type", "application/json")
    //             .GET()
    //             .build();
    //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    //     // Parse response and map to List<AnimeDto>
    //     // Assuming the response structure has a 'data' field containing the list
    //     JikanSearchResponse searchResponse = gson.fromJson(response.body(), JikanSearchResponse.class);
    //     List<Anime> animeList = searchResponse.getData().stream()
    //             .map(MapperUtil::mapToAnime)
    //             .collect(Collectors.toList());
    //     return animeList;
    // }

    // public List<Anime> getTopAnime(int limit) throws Exception {
    //     String endpoint = baseApi + "/top/anime"+ "?limit=" + limit;
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create(endpoint))
    //             .header("Content-Type", "application/json")
    //             .GET()
    //             .build();
    //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    //     // Parse response and map to List<AnimeDto>
    //     JikanTopResponse topResponse = gson.fromJson(response.body(), JikanTopResponse.class);
    //     List<Anime> animeList = topResponse.getData().stream()
    //             .map(MapperUtil::mapToAnime)
    //             .collect(Collectors.toList());
    //     return animeList;
    // }




    public String getSearchAnimeEndpoint(String query) {
        return baseApi + "/anime?q=" + query;
    }

    public String getTopAnimeEndpoint(int limit) {
        return baseApi + "/top/anime"+ "?limit=" + limit;
    }

    public String getAnimeByIdEndpoint(int id) {
        return baseApi + "/anime/" + id;
    }

}
