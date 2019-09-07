package com.oip.helpdesk.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "knowledge")
public class Knowledge extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "title")
    private String title;
	
	@Column(name = "description")
    private String description;
	
	
	/* *** *** *** ***  RELATION DB KNOWLEDGE - SERVICES*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Service service;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "area_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Area area;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Service getService() {
		return service;
	}


	public void setService(Service service) {
		this.service = service;
	}


	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
