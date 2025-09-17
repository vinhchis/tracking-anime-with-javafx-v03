package com.project.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Seasons", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "season_name", "season_year" })
})
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "season_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "season_name", length = 10, nullable = false)
    private SEASON_NAME seasonName;

    @Column(name = "season_year", nullable = false)
    private Short seasonYear;

    // --- Relationships ---
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<Anime> animes;

    public enum SEASON_NAME {
        WINTER, SPRING, SUMMER, FALL
    }
}