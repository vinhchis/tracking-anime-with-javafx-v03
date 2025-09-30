package com.project.dto;

import java.time.LocalTime;

import com.project.entity.Tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingScheduleCardDto {
    private String title;
    private String posterUrl;
    private Short lastWatchedEpisode;
    private Short totalEpisodes;
    private Tracking.DAY_OF_WEEK scheduleDay;
    private LocalTime scheduleLocalTime;

    public void setLastWatchedEpisode(Short lastWatchedEpisode) {
        if(lastWatchedEpisode == null) {
            this.lastWatchedEpisode = 0;
            return;
        }
        this.lastWatchedEpisode = lastWatchedEpisode;
    }
    public void setTotalEpisodes(Short totalEpisodes) {
        if(totalEpisodes == null) {
            this.totalEpisodes = 0;
            return;
        }
        this.totalEpisodes = totalEpisodes;
    }
}
