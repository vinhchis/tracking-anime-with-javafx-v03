package com.project.repository;

import com.project.entity.Notification;

public class NotificationRepository extends JpaRepository<Notification, Integer> {
    public NotificationRepository() {
        super();
    }

}
