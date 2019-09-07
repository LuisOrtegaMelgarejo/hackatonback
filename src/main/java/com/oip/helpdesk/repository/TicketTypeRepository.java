package com.oip.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oip.helpdesk.domain.entities.TicketType;;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long>{

}
