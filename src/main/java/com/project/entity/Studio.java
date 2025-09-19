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
@Table(name = "Studios")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_id")
    private Integer id;

    @Column(name = "studio_name", length = 100, nullable = false, unique = true)
    private String studioName;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "best_animes", columnDefinition = "NVARCHAR(MAX)")
    private String bestAnimes;

    // --- Relationships ---
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Anime> animes;
}