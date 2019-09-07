package com.oip.helpdesk.domain.entities;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "priority")
public class Priority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name; //Baja, Media, Alta
	
	@Column(name="time")
	private Double time=0.00;
		

  	private Priority() {
	
	}
	
	private Priority(Long id, String name, Double time,List<Ticket> tickets) {
		this.id = id;
		this.name = name;
		this.time = time;
		//this.tickets = tickets;
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

	
	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	
	
}
