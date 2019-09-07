package com.oip.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.entities.Area;
import com.oip.helpdesk.domain.entities.Priority;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.PriorityRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PriorityController {
	
	@Autowired
	PriorityRepository priorityRepository;
	
	@GetMapping("/priority")
	public List<Priority> getAllPriority() {	
		return this.priorityRepository.findAll();
		
	}
	
	@PostMapping("/priority/create")
	public Priority createPriority(@RequestBody Priority priority) {

		return priorityRepository.save(priority);
	}
	
	@PutMapping("/priority/{priorityId}/update")	
	public Priority updatePriority(@PathVariable("priorityId") Long priorityId, @Valid @RequestBody Priority priorityData) {
	
		Priority priority = priorityRepository.findById(priorityId).orElseThrow(()-> new ResourceNotFoundException("Priority","priority_id",priorityId));
        priority.setName(priorityData.getName());
        priority.setTime(priorityData.getTime());
					
		Priority updatePriority = priorityRepository.save(priority);
		return updatePriority;		
	}

	@DeleteMapping("/priority/{priorityId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("priorityId") Long priorityId) {

		if(!priorityRepository.existsById(priorityId)) {
			throw new ResourceNotFoundException("areaId","areaId", priorityId);
		}

		return priorityRepository.findById(priorityId).map(channel -> {
			priorityRepository.delete(channel);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("areaId","areaId", priorityId));

	}
}
