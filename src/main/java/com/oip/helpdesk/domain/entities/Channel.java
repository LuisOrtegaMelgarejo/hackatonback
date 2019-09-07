package com.oip.helpdesk.domain.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "channel")
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name; //Telefono, Email, Verbal
	
	 /* *** *** *** ***  RELATION CHANNEL - TICKET*** *** *** *** ***/  	
	@OneToMany(mappedBy="channel", cascade = CascadeType.ALL)
	@JsonIgnore	
	private List<Ticket> tickets = new ArrayList<>();
	
	private Channel() {
		
	}
	

	private Channel(Long id, String name, List<Ticket> tickets) {
		this.id = id;
		this.name = name;
		this.tickets = tickets;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Ticket> getTickets() {
		return tickets;
	}


	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	
	

}
