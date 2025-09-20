package com.project.repository;

import com.project.entity.Tracking;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class TrackingRepositoryImpl implements TrackingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long countAll() {
        return entityManager.createQuery("SELECT COUNT(t) FROM Tracking t", Long.class)
                .getSingleResult();
    }

    @Override
    public List<Tracking> findByStatus(Tracking.TrackingStatus status) {
        return entityManager.createQuery("SELECT t FROM Tracking t WHERE t.trackingStatus = :status", Tracking.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Tracking> findAll() {
        return entityManager.createQuery("SELECT t FROM Tracking t", Tracking.class)
                .getResultList();
    }
}