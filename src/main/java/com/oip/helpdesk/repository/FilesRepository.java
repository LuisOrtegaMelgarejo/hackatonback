package com.oip.helpdesk.repository;


import com.oip.helpdesk.domain.entities.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FilesRepository extends JpaRepository<Files, Long> {
    Files findByRandonname(String randomname);
}
