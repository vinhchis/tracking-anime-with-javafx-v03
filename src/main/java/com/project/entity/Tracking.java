package com.project.entity;

import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tracking")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_status", length = 15)
    private TRACKINGS_STATUS trackingStatus;

    @Column(name = "last_watched_episode")
    private Short lastWatchedEpisode;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_day", length = 10)
    private DAY_OF_WEEK scheduleDay;

    @Column(name = "schedule_time")
    private LocalTime scheduleTime;

    @Column(name = "rating")
    private Byte rating;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;


    public enum TRACKINGS_STATUS {
        WATCHING, COMPLETED, ON_HOLD, DROPPED, PLAN_TO_WATCH
    }

    public enum DAY_OF_WEEK {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
