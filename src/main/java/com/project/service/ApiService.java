package com.project.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.project.dto.AnimeCardDto;

import com.project.dto.JikanAnimeResponse;
import com.project.dto.JikanAnimeResponse.AnimeData;
import com.project.dto.JikanSearchResponse;

import com.project.util.MapperUtil;

public class ApiService {
    private final String baseApi = "https://api.jikan.moe/v4";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ApiService() {

    }

    public List<AnimeCardDto> searchAnimeCardDto(String query, int limit) throws Exception {
        List<AnimeData> animeDataList = searchAnime(query, limit);
        return animeDataList.stream()
                .map((data) -> MapperUtil.mapToAnimeCardDto(data))
                .toList();
    }

    public List<AnimeCardDto> getTopAnimeCardDto(int limit, String filter, String type) throws Exception {
        List<AnimeData> animeDataList = getTopAnime(limit, filter, type);
        return animeDataList.stream()
                .map((data) -> MapperUtil.mapToAnimeCardDto(data))
                .toList();
    }

    private List<JikanAnimeResponse.AnimeData> searchAnime(String query, int limit) throws Exception {
        String endpoint = baseApi + "/anime?q=" + query + "&limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JikanSearchResponse searchResponse = gson.fromJson(response.body(), JikanSearchResponse.class);
        return searchResponse.getData();
    }

    // https://api.jikan.moe/v4/top/anime?limit=10&filter=airing&type=tv
    private List<JikanAnimeResponse.AnimeData> getTopAnime(int limit, String filter, String type) throws Exception {
        String endpoint = baseApi + "/top/anime" + "?limit=" + limit + "&filter=" + filter + "&type=" + type;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JikanSearchResponse topResponse = gson.fromJson(response.body(), JikanSearchResponse.class);
        return topResponse.getData();
    }


}