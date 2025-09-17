package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingCardDto {
    // from tracking
    private int trackingId;
    private String trackingStatus;
    private String animeTitle;
    private short lastWatchedEpisode;
    private byte rating;
    private String note;

    // from anime
    private int animeId;
    private String animeStatus;
    private String animeType;
    private String imageUrl;
    private short totalEpisodes;

    // from studio
    private String studioName;

    // from season
    private String seasonName;
    private short seasonYear;

    // from episode
    private short currentEpisode;

}
