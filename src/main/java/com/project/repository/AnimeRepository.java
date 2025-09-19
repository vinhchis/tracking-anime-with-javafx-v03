package com.project.repository;

import com.project.entity.Anime;

public class AnimeRepository extends JpaRepository<Anime, Integer> {
    public AnimeRepository() {
        super();
    }
}
