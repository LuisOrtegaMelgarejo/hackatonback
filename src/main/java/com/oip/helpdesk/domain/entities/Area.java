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

@Entity
@Table(name = "area")
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name; //Logistica, Informatica,etc

	/* *** *** *** ***  RELATION USERS - AREA *** *** *** *** ***/ 	
	/*@OneToMany(mappedBy="area", cascade = CascadeType.ALL)
	private List<User> users = new ArrayList<>();*/

	private Area() {
		
	}
	
	private Area( String name, List<User> users) {
		
		this.name = name;
		//this.users = users;
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

}
