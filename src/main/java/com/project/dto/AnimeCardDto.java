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
    private int animeId;
    private String title;
    private String posterUrl;
    private String animeStatus;
    private String animeType;
    private Short totalEpisodes;
    private String scheduleDay;
    private String scheduleTime;
    private String studioName;
    private String seasonName;
    private int seasonYear;

    //
    private int studioId;

}
