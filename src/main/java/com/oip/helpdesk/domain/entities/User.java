package com.oip.helpdesk.domain.entities;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
@Table(name = "users")

public class User extends AuditModel{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "last_name")
	private String last_name;
	
	@Email(message = "Ingrese una dirección de email valida")
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private boolean active=true;
	
	

	/** *** *** *** *** ***  RELATION  USER - ROLES *** *** *** *** *** */	

     @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    /* *** *** *** ***  RELATION USERS - AREA *** *** *** *** ** */ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "area_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)		
	private Area area;
	

	/* *** *** *** ***  RELATION USER - USERTYPE*** *** *** *** ***/
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserType type;
	

   	
  	
	public User() {
	}
	
			
	public User(User user) {
		
		this.id = user.getId();
		this.name = user.getName();
		this.last_name = user.getLast_name();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.active = user.isActive();		
		this.roles = user.getRoles();
		this.area = user.getArea();
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


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}
