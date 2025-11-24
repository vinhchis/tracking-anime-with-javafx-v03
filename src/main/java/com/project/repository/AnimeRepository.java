package com.project.repository;

import java.util.List;

import com.project.entity.Anime;

public class AnimeRepository extends JpaRepository<Anime, Long> {
    public AnimeRepository() {
        super();
    }

    public List<Integer> getAllAnimeApiId() {
        return super.emf.createEntityManager().createQuery("SELECT a.apiId FROM Anime a", Integer.class)
                .getResultList();
    }

}
