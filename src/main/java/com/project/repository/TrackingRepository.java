package com.project.repository;

import java.util.List;

import com.project.entity.Tracking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class TrackingRepository extends JpaRepository<Tracking, Long> {
    public TrackingRepository() {
        super();
    }

    public List<Tracking> getTrackingFullInfo() {
        EntityManager em = super.emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tracking t "
                    + "LEFT JOIN FETCH t.anime a "
                    + "LEFT JOIN FETCH a.studio s "
                    + "LEFT JOIN FETCH a.season se", Tracking.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
