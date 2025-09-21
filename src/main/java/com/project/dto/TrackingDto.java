package com.project.dto;

import java.time.LocalTime;

import com.project.entity.Anime.ANIME_STATUS;
import com.project.entity.Anime.ANIME_TYPE;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;

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
    private Long trackingId;
    private TRACKINGS_STATUS trackingStatus;
    private Short lastWatchedEpisode;
    private DAY_OF_WEEK scheduleDay;
    private LocalTime scheduleLocalTime;
    private Byte rating;
    private String note;

    // from anime
    private Long animeId;
    private Integer apiId;
    private String animeTitle;
    private ANIME_STATUS animeStatus;
    private ANIME_TYPE animeType;
    private String imageUrl;
    private Short totalEpisodes;

    // from studio
    private String studioName;

    // from season
    private String seasonName;
    private Short seasonYear;


}
