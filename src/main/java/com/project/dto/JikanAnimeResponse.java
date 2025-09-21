package com.project.dto;


import lombok.Data;
import java.util.List;

@Data
public class JikanAnimeResponse {
    private AnimeData data;

    @Data
    public static class AnimeData {
        private int mal_id; // 52991
        private String url; // "https://myanimelist.net/anime/52991/Sousou_no_Frieren"
        private String title; // "Sousou no Frieren"
        private String synopsis; // "During their decade-long quest to defeat..."
        private String type; // TV
        private String status; // Finished Airing
        private Integer episodes; // 28
        private Images images;
        private List<Studio> studios;
        private String season;   // "spring", "summer", ...
        private Integer year;    // 1998
        private Double score;    // 9.29
    }

    @Data
    public static class Images {
        private Jpg jpg;
    }

    @Data
    public static class Jpg {
        private String image_url; // "https://cdn.myanimelist.net/images/anime/1015/138006.jpg"
    }

    @Data
    public static class Studio {
        private int mal_id; // 11
        private String name; // Madhouse
    }
}
