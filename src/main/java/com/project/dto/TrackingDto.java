package com.project.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingDto {
    // from tracking
    private int trackingId;
    private String trackingStatus;
    private short lastWatchedEpisode;
    private String scheduleDay;
    private LocalTime scheduleLocalTime;
    private byte rating;
    private String note;

    // from anime
    private int animeId;
    private String apiId;
    private String animeTitle;
    private String animeStatus;
    private String animeType;
    private String imageUrl;
    private short totalEpisodes;

    // from studio
    private String studioName;

    // from season
    private String seasonName;
    private short seasonYear;


}
