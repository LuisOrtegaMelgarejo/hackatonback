package com.oip.helpdesk.repository;


import com.oip.helpdesk.domain.entities.Knowledge;
import com.oip.helpdesk.domain.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    @Query(
            value = "SELECT * FROM knowledge WHERE area_id = ?1",
            nativeQuery = true)
    List<Knowledge> findByAreaId (Long area_id);

}