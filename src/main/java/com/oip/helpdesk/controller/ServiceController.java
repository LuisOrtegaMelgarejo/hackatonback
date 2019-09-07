package com.oip.helpdesk.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.oip.helpdesk.domain.entities.ConfigurationItem;
import com.oip.helpdesk.repository.ConfigurationItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.entities.Area;
import com.oip.helpdesk.domain.entities.Service;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.ServiceRepository;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class ServiceController {
	
	@Autowired
	ServiceRepository serviceRepository;

	@Autowired
	ConfigurationItemRepository configurationItemRepository;


	@GetMapping("/services")
	public List<Service> getAllService() {
		return this.serviceRepository.findAll();
	}


	@GetMapping("/services/{configurationItemId}")
	public List<Service> getService(@PathVariable(value = "configurationItemId") Long id) {
		return this.serviceRepository.findByConfigurationItemId(id);
	}

	@PostMapping("/services/create")
	public Service createService(@RequestBody HashMap<String,String> body) {
		Service service = new Service();
		service.setName(body.get("name"));
		ConfigurationItem ci =  configurationItemRepository.findById(new Long(body.get("configuration_item_id"))).orElseThrow(()-> new ResourceNotFoundException("ConfigurationItem","configuration_item_id",body.get("configuration_item_id")));
		service.setConfigurationItem(ci);
		return serviceRepository.save(service);
	}

	@PutMapping("/services/{serviceId}/update")
	
	public Service updateService(@PathVariable("serviceId") Long serviceId,@RequestBody HashMap<String,String> body) {
	
		Service service = serviceRepository.findById(serviceId).orElseThrow(()-> new ResourceNotFoundException("Service","service_id",serviceId));
		service.setName(body.get("name"));
		ConfigurationItem ci =  configurationItemRepository.findById(new Long(body.get("configuration_item_id"))).orElseThrow(()-> new ResourceNotFoundException("ConfigurationItem","configuration_item_id",body.get("configuration_item_id")));
		service.setConfigurationItem(ci);

		Service updateService = serviceRepository.save(service);
		return updateService;		
	}

	@DeleteMapping("/services/{serviceId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("serviceId") Long serviceId) {

		if(!serviceRepository.existsById(serviceId)) {
			throw new ResourceNotFoundException("serviceId","serviceId", serviceId);
		}

		return serviceRepository.findById(serviceId).map(channel -> {
			serviceRepository.delete(channel);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("serviceId","serviceId", serviceId));

	}
}
