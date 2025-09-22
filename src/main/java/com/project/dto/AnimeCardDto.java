package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnimeCardDto {
    private Long animeId; // can be null for new anime
    private Integer apiId;
    private String title;
    private String synopsis;
    private String url;
    private String posterUrl;
    private String animeStatus;
    private String animeType;
    private Short totalEpisodes;
    private Double score;

    private String studioName; // can be null

    private String seasonName; // can be null
    private Short seasonYear; // can be null
}
