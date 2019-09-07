package com.oip.helpdesk.repository;

import com.oip.helpdesk.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oip.helpdesk.domain.entities.Area;

import java.util.List;


@Repository

public interface AreaRepository extends JpaRepository<Area, Long> {

}
