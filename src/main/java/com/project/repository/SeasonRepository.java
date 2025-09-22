package com.project.repository;

import java.util.Optional;

import com.project.entity.Season;

import jakarta.persistence.EntityManager;

public class SeasonRepository extends JpaRepository<Season, Integer> {
    public SeasonRepository() {
        super();
    }

    public Optional<Season> findBySeasonNameAndSeasonYear(Season.SEASON_NAME seasonName, Short seasonYear) {
        EntityManager em = super.emf.createEntityManager();
        String jpql = "SELECT s FROM Season s WHERE s.seasonName = :seasonName AND s.seasonYear = :seasonYear";
        return em.createQuery(jpql, Season.class)
                .setParameter("seasonName", seasonName)
                .setParameter("seasonYear", seasonYear)
                .getResultStream()
                .findFirst();
    }
}