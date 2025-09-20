package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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

    @Column(name = "showtime")
    private LocalDateTime showtime;

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
}
