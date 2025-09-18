package com.project.dto;


import lombok.Data;
import java.util.List;

@Data
public class JikanAnimeResponse {
    private AnimeData data;

    @Data
    public static class AnimeData {
        private int mal_id;
        private String title;
        private String synopsis;
        private String type;
        private String status;
        private Integer episodes;
        private Images images;
        private List<Studio> studios;
        private String season;   // "spring", "summer", ...
        private Integer year;    // 1998
    }

    @Data
    public static class Images {
        private Jpg jpg;
    }

    @Data
    public static class Jpg {
        private String image_url;
    }

    @Data
    public static class Studio {
        private int mal_id;
        private String name;
    }
}
