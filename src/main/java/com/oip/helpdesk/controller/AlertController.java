package com.oip.helpdesk.controller;

import com.oip.helpdesk.domain.entities.Alert;
import com.oip.helpdesk.domain.entities.Area;
import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.AlertRepository;
import com.oip.helpdesk.repository.AreaRepository;
import com.oip.helpdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class AlertController {

	@Autowired
	AlertRepository alertRepository;
	@Autowired
	UserService userService;

	@Transactional
	@GetMapping("/alerts")
	public List<Alert> getAlertsByUser(@RequestHeader("Authorization") String header){
		User user = userService.findByToken(header);
		this.alertRepository.updateNotified(user.getId());
		return this.alertRepository.findByUserId(user.getId());
	}

	@Transactional
	@GetMapping("/alerts/new")
	public List<Alert> getAlertsNotNotByUser(@RequestHeader("Authorization") String header){
		User user = userService.findByToken(header);
		List<Alert> data = this.alertRepository.findNotNotifiedByUserId(user.getId());
		this.alertRepository.updateNotified(user.getId());
		return data;
	}

	@Transactional
	@PatchMapping("/alerts")
	public ResponseEntity<?> setSeen(@RequestHeader("Authorization") String header){
		User user = userService.findByToken(header);
		this.alertRepository.updateSeen(user.getId());
		return ResponseEntity.ok().build();
	}
}
