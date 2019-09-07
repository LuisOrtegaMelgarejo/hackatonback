package com.oip.helpdesk.controller;

import com.oip.helpdesk.domain.entities.ConfigurationItem;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.ConfigurationItemRepository;
import com.oip.helpdesk.repository.ConfigurationItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class ConfigurationItemController {
	
	@Autowired
	ConfigurationItemRepository configurationItemRepository;


	@GetMapping("/configuration")
	public List<ConfigurationItem> getAllConfigurationItem() {
		return this.configurationItemRepository.findAll();
	}

	@PostMapping("/configuration/create")
	public ConfigurationItem createConfigurationItem(@RequestBody ConfigurationItem service) {

		return configurationItemRepository.save(service);
	}
	
	@GetMapping("/configuration/{configurationId}")
	public ConfigurationItem getConfigurationItemById(@PathVariable(value = "configurationId") Long configurationId){		
		 return configurationItemRepository.findById(configurationId).orElseThrow(()-> new ResourceNotFoundException("ConfigurationItem","service_id",configurationId));
		
	}
	
	@PutMapping("/configuration/{configurationId}/update")
	public ConfigurationItem updateConfigurationItem(@PathVariable("configurationId") Long configurationId, @Valid @RequestBody ConfigurationItem serviceData) {
	
		ConfigurationItem service = configurationItemRepository.findById(configurationId).orElseThrow(()-> new ResourceNotFoundException("ConfigurationItem","service_id",configurationId));
        service.setName(serviceData.getName());		
					
		ConfigurationItem updateConfigurationItem = configurationItemRepository.save(service);
		return updateConfigurationItem;		
	}

	@DeleteMapping("/configuration/{configurationId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("configurationId") Long configurationId) {

		if(!configurationItemRepository.existsById(configurationId)) {
			throw new ResourceNotFoundException("configurationId","configurationId", configurationId);
		}

		return configurationItemRepository.findById(configurationId).map(channel -> {
			configurationItemRepository.delete(channel);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("configurationId","configurationId", configurationId));

	}
}
