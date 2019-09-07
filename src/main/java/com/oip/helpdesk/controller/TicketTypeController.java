package com.oip.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.entities.TicketType;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.TicketTypeRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TicketTypeController {
	@Autowired
	TicketTypeRepository ticketTypeRepository;
	
	@GetMapping("/types")
	public List<TicketType> getAllTicketTypes() {	
		return this.ticketTypeRepository.findAll();
		
	}

	@PostMapping("/types/create")
	public TicketType createTicketType(@RequestBody TicketType ticketType) {
		return ticketTypeRepository.save(ticketType);
	}
	
	
	@GetMapping("/types/{ticketTypeId}")
	public TicketType getTicketTypeById(@PathVariable(value = "ticketTypeId") Long ticketTypeId){		
		 return ticketTypeRepository.findById(ticketTypeId).orElseThrow(()-> new ResourceNotFoundException("TicketType","ticketType_id", ticketTypeId));
		
	}
	
	@PutMapping("/types/{ticketTypeId}/update")
	public TicketType  updateTicketType(@PathVariable("ticketTypeId") Long ticketTypeId, @Valid @RequestBody TicketType  ticketTypeData) {
	
		TicketType  ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(()-> new ResourceNotFoundException("ticketType","channel_id",ticketTypeId));
		ticketType.setName(ticketTypeData.getName());					
		TicketType  updateTicketType = ticketTypeRepository.save(ticketType);
		
		return updateTicketType;		
	}

	@DeleteMapping("/types/{ticketTypeId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("ticketTypeId") Long ticketTypeId) {

		if(!ticketTypeRepository.existsById(ticketTypeId)) {
			throw new ResourceNotFoundException("areaId","areaId", ticketTypeId);
		}

		return ticketTypeRepository.findById(ticketTypeId).map(channel -> {
			ticketTypeRepository.delete(channel);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("areaId","areaId", ticketTypeId));

	}
}
