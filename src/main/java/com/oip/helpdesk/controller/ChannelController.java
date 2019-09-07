package com.oip.helpdesk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oip.helpdesk.domain.entities.Channel;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.ChannelRepository;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ChannelController {

	@Autowired
	ChannelRepository channelRepository;
	
	@GetMapping("/channels")
	public List<Channel> getAllChannel() {	
		return this.channelRepository.findAll();
		
	}

	@PostMapping("/channels/create")
	public Channel createChannel(@RequestBody Channel channel) {
		return channelRepository.save(channel);
	}
	
	
	@GetMapping("/channels/{channelId}")
	public Channel getChannelById(@PathVariable(value = "channelId") Long channelId){		
		 return channelRepository.findById(channelId).orElseThrow(()-> new ResourceNotFoundException("Channel","channel_id", channelId));
		
	}
	
	@PutMapping("/channels/{channelId}/update")
	public Channel updateCatalog(@PathVariable("channelId") Long channelId, @Valid @RequestBody Channel channelData) {
	
		Channel channel = channelRepository.findById(channelId).orElseThrow(()-> new ResourceNotFoundException("Channel","channel_id",channelId));
		channel.setName(channelData.getName());					
		Channel updateChannel = channelRepository.save(channel);
		
		return updateChannel;		
	}
	
	
	@DeleteMapping("/channels/{channelId}")
	public ResponseEntity<?> deleteChannel(@PathVariable("channelId") Long channelId) {
		
		if(!channelRepository.existsById(channelId)) {
            throw new ResourceNotFoundException("Cahnnel","channel_id", channelId);
        }

        return channelRepository.findById(channelId).map(channel -> {
        	channelRepository.delete(channel);
             return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Channel","channel_id", channelId));
		
	}
	
	
	
}
