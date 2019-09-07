package com.oip.helpdesk.domain.entities;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
public class Comment extends AuditModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "text")
	private String text;

	@Column(name = "ticket_id")
	private Long ticketId;
	 
	 /* *** *** *** ***  RELATIONS  COMMENT - USER*** *** *** *** ***/
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	/* *** *** *** ***  RELATION TICKET - COMMENTS*** *** *** *** ***/
	@OneToMany(mappedBy = "comment_id",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Files> files = new HashSet<>();

	public Comment() {
			
	 }

	private Comment(Long id, String text, Long ticket_id, User user) {
		this.id = id;
		this.text = text;
		this.user = user;
		this.ticketId = ticket_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getTicket_id() {
		return ticketId;
	}

	public void setTicket_id(Long ticket_id) {
		this.ticketId = ticket_id;
	}

	public Set<Files> getFiles() {
		return files;
	}

	public void setFiles(Set<Files> files) {
		this.files = files;
	}
}
