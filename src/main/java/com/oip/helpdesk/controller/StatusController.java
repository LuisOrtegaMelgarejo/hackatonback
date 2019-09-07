package com.oip.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oip.helpdesk.domain.entities.Channel;
import com.oip.helpdesk.domain.entities.Status;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.TicketStatusRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class StatusController {
	
	@Autowired
	TicketStatusRepository ticketStatusRepository;


	@GetMapping("/status")

	public List<Status> getAllStatus() {	
		return this.ticketStatusRepository.findAll();
		
	}
	
		
	@PostMapping("/status/create")	
	public Status createStatus(@RequestBody Status status) {

		return ticketStatusRepository.save(status);
	}
	
	
	@GetMapping("/status/{statusId}")
	public Status getStatusById(@PathVariable(value = "statusId") Long statusId){		
		 return ticketStatusRepository.findById(statusId).orElseThrow(()-> new ResourceNotFoundException("Status","status", statusId));
		
	}
	
	@PutMapping("/status/{statusId}/update")
	public Status updateStatus(@PathVariable("statusId") Long statusId, @Valid @RequestBody Status statusData) {
	
		Status ticketStatus = ticketStatusRepository.findById(statusId).orElseThrow(()-> new ResourceNotFoundException("status","status_id",statusId));

		ticketStatus.setName(statusData.getName());					
		Status updateStatus = ticketStatusRepository.save(ticketStatus);
		
		return updateStatus;		
	}

	@GetMapping("/status/{ticketId}/status")

	public List<Status> getStatusByTicket() {
		return this.ticketStatusRepository.findAll();

	}

}
