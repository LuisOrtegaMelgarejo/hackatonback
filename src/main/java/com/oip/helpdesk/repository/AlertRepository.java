package com.oip.helpdesk.repository;

import com.oip.helpdesk.domain.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query(value = "SELECT * FROM alerts WHERE user_id = ?1 ORDER BY created_at DESC",nativeQuery = true)
    List<Alert> findByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE alerts SET seen = 1 WHERE user_id = ?1",nativeQuery = true)
    void updateSeen (Long userId);

    @Modifying
    @Query(value = "UPDATE alerts SET notified = 1 WHERE user_id = ?1",nativeQuery = true)
    void updateNotified (Long userId);

    @Query(value = "SELECT * FROM alerts WHERE user_id = ?1 AND notified = 0 ORDER BY created_at DESC",nativeQuery = true)
    List<Alert> findNotNotifiedByUserId(Long userId);
}
