package com.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anime_id")
    private Long id;

    @Column(name = "api_id", unique = true, nullable = true)
    private Integer apiId; // Jikan API ID

    @Column(length = 50, nullable = false)
    private String title;

    @Column(name = "poster_url", length = 255)
    private String posterUrl;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "anime_status", length = 15)
    private ANIME_STATUS animeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "anime_type", length = 10)
    private ANIME_TYPE animeType;

    @Column(name = "total_episodes")
    private Short totalEpisodes;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;


    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Tracking> trackings;

    public enum ANIME_STATUS {
        ONGOING, COMPLETED, HIATUS, CANCELLED, UPCOMING
    }

    public enum ANIME_TYPE {
        TV, MOVIE, OVA, SPECIAL
    }


}