package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_read", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isRead;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_id", nullable = true)
    private Tracking tracking;

    @Column(name = "notifiable_type")
    @Enumerated(EnumType.STRING)
    private NOTIFY_TYPE notifiableType;

    public enum NOTIFY_TYPE {
        EPISODE_RELEASE,
        TRACKING_UPDATE,
        GENERAL_ANNOUNCEMENT
    }
}