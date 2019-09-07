package com.oip.helpdesk.controller;

import com.oip.helpdesk.domain.entities.*;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.AreaRepository;
import com.oip.helpdesk.repository.ChannelRepository;
import com.oip.helpdesk.repository.KnowledgeRepository;
import com.oip.helpdesk.repository.ServiceRepository;
import com.oip.helpdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class KnowledgelController {

	@Autowired
	KnowledgeRepository knowledgeRepository;
	@Autowired
	UserService userService;
	@Autowired
	ServiceRepository serviceRepository;
	@Autowired
	AreaRepository areaRepository;

	@GetMapping("/knowledge")
	public List<Knowledge> getAllChannel() {
		return this.knowledgeRepository.findAll();
	}

	@GetMapping("/knowledgeByUser")
	public List<Knowledge> getKnowledge(@RequestHeader("Authorization") String header) {
		User user = userService.findByToken(header);
		return this.knowledgeRepository.findByAreaId(user.getArea().getId());
	}

	@PostMapping("/knowledge/create")
	public ResponseEntity<?> createChannel(@RequestBody HashMap<String,String> body) {
		Knowledge know = new Knowledge();
		know.setDescription(body.get("description"));
		know.setTitle(body.get("title"));
		Service service = serviceRepository.findById(Long.parseLong(body.get("service_id"))).orElseThrow(() -> new ResourceNotFoundException("Priority","priority_id", body.get("priority_id")));
		know.setService(service);
		Area area = areaRepository.findById(new Long(body.get("area_id"))).orElseThrow(()-> new ResourceNotFoundException("Area","area_id",body.get("area_id")));
		know.setArea(area);
		knowledgeRepository.save(know);
		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/knowledge/{knowledgeId}")
	public Knowledge getChannelById(@PathVariable(value = "knowledgeId") Long knowledgeId){
		 return knowledgeRepository.findById(knowledgeId).orElseThrow(()-> new ResourceNotFoundException("Knowledge","knowledgeId", knowledgeId));
	}
	
	@PutMapping("/knowledge/{knowledgeId}/update")
	public ResponseEntity<?> updateCatalog(@PathVariable("knowledgeId") Long knowledgeId,
								  @RequestBody HashMap<String,String> body) {

		Knowledge know =  knowledgeRepository.findById(knowledgeId).orElseThrow(() -> new ResourceNotFoundException("Knowledge","knowledgeId", knowledgeId));
		know.setDescription(body.get("description"));
		know.setTitle(body.get("title"));
		Service service = serviceRepository.findById(Long.parseLong(body.get("service_id"))).orElseThrow(() -> new ResourceNotFoundException("Priority","priority_id", body.get("priority_id")));
		know.setService(service);
		Area area = areaRepository.findById(new Long(body.get("area_id"))).orElseThrow(()-> new ResourceNotFoundException("Area","area_id",body.get("area_id")));
		know.setArea(area);
		knowledgeRepository.save(know);
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/knowledge/{knowledgeId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("knowledgeId") Long knowledgeId) {
		
		if(!knowledgeRepository.existsById(knowledgeId)) {
            throw new ResourceNotFoundException("Knowledge","knowledgeId", knowledgeId);
        }

        return knowledgeRepository.findById(knowledgeId).map(channel -> {
			knowledgeRepository.delete(channel);
             return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Knowledge","knowledgeId", knowledgeId));
		
	}
	
	
	
}
