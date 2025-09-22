package com.project.dto;

import java.time.LocalTime;

import com.project.entity.Season;
import com.project.entity.Tracking;
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
    private Season.SEASON_NAME seasonName;
    private Short seasonYear;

    public void setTrackingStatus(TRACKINGS_STATUS trackingStatus) {
        if(trackingStatus == null) {
            this.trackingStatus = TRACKINGS_STATUS.WATCHING;
            return;
        }
        this.trackingStatus = trackingStatus;
    }
    public void setLastWatchedEpisode(Short lastWatchedEpisode) {
        if(lastWatchedEpisode == null) {
            this.lastWatchedEpisode = 0;
            return;
        }
        this.lastWatchedEpisode = lastWatchedEpisode;
    }

    public void setScheduleDay(DAY_OF_WEEK scheduleDay) {
        if(scheduleDay == null) {
            this.scheduleDay = DAY_OF_WEEK.SUNDAY;
            return;
        }
        this.scheduleDay = scheduleDay;
    }
    public void setScheduleLocalTime(LocalTime scheduleLocalTime) {
        if(scheduleLocalTime == null) {
            this.scheduleLocalTime = LocalTime.of(20, 0); // default 8 PM
            return;
        }
        this.scheduleLocalTime = scheduleLocalTime;
    }
    public void setRating(Byte rating) {
        if(rating == null) {
            this.rating = 0;
            return;
        }
        this.rating = rating;
    }
    public void setNote(String note) {
        if(note == null) {
            this.note = "";
            return;
        }
        this.note = note;
    }



}
