package com.oip.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import com.oip.helpdesk.domain.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.entities.Area;
import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.AreaRepository;



@CrossOrigin
@RestController
@RequestMapping("/api")
public class AreaController {
	@Autowired
	AreaRepository areaRepository;


	@GetMapping("/areas")

	public List<Area> getAllArea() {	
		return this.areaRepository.findAll();
		
	}
	
	@PostMapping("/areas/create")
	public Area createArea(@RequestBody Area area) {

		return areaRepository.save(area);
	}
	
	@GetMapping("/areas/{areaId}")
	public Area getAreaById(@PathVariable(value = "areaId") Long areaId){		
		 return areaRepository.findById(areaId).orElseThrow(()-> new ResourceNotFoundException("User","user_id", areaId));
		
	}
	
	@PutMapping("/areas/{areaId}/update")
	
	public Area updateArea(@PathVariable("areaId") Long areaId, @Valid @RequestBody Area areaData) {
	
		Area area = areaRepository.findById(areaId).orElseThrow(()-> new ResourceNotFoundException("Area","area_id",areaId));
        area.setName(areaData.getName());		
					
		Area updateArea = areaRepository.save(area);
		return updateArea;		
	}


	@DeleteMapping("/areas/{areaId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("areaId") Long areaId) {

		if(!areaRepository.existsById(areaId)) {
			throw new ResourceNotFoundException("areaId","areaId", areaId);
		}

		return areaRepository.findById(areaId).map(channel -> {
			areaRepository.delete(channel);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("areaId","areaId", areaId));

	}
}
