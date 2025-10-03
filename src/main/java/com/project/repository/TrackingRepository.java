package com.project.repository;

import java.util.List;

import com.project.entity.Tracking;

import jakarta.persistence.EntityManager;

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

    public List<Tracking> getTrackingsForSchedule() {
        EntityManager em = super.emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tracking t "
                    + "LEFT JOIN FETCH t.anime a ", Tracking.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    //ham duoc them lan 1
    public List<Tracking> getScheduledTrackings() {
        EntityManager em = super.emf.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tracking t "
                            + "LEFT JOIN FETCH t.anime a "
                            + "WHERE t.scheduleDay IS NOT NULL", Tracking.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
