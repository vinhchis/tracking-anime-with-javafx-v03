package com.project.entity;

import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_status", length = 15)
    private TrackingStatus trackingStatus;

    @Column(name = "last_watched_episode")
    private Short lastWatchedEpisode;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_day", length = 10)
    private DAY_OF_WEEK scheduleDay;

    @Column(name = "schedule_time")
    private LocalTime scheduleTime;

    @Column(name = "rating", columnDefinition = "BIT DEFAULT 0")
    private Byte rating;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String note;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;


    public enum TrackingStatus {
        WATCHING, COMPLETED, ON_HOLD, DROPPED, PLAN_TO_WATCH
    }

    public enum DAY_OF_WEEK {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
