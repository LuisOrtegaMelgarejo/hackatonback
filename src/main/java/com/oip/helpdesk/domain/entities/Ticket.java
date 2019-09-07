package com.oip.helpdesk.domain.entities;



import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OrderBy;

@Entity
@Table(name = "ticket")
public class Ticket extends AuditModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "description", nullable=false)
	private String description;
	
	@Column(name = "image")
	private String image;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "solved_at", nullable = true)   
    private Date solved_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "close_at", nullable = true)   
    private Date close_at;
	
	@Column(name="sla")
	private boolean sla = false;
	
	
	@Column(name= "deleted")
	private Character deleted;

	@Column(name= "title")
	private String title;
	


   /* *** *** *** ***  RELATION USER-TICKET*** *** *** *** ***/  
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "assigned_to", nullable = true)
	private User asignado;

	/* *** *** *** ***  RELATION TICKET -TICKETSTATUS*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Status status;
	
	/* *** *** *** ***  RELATION TICKET VS TICKET-TYPE*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private TicketType type;
	
	/* *** *** *** ***  RELATION TICKET - PRIORITY*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "priority_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Priority priority;
	
	/* *** *** *** ***  Relation ticket - channel*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "channel_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Channel channel;
	
	/* *** *** *** ***  RELATION TICKET - AREA*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "area_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Area area;
	
	/* *** *** *** ***  RELATION TICKET - SERVICES*** *** *** *** ***/ 
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Service service;

	/* *** *** *** ***  RELATION TICKET - COMMENTS*** *** *** *** ***/
	@OneToMany(mappedBy = "ticketId",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	//@OrderBy("kittenName")
	private Set<Comment> comments = new HashSet<>();





	public Ticket() {
	}


	public Ticket(Ticket ticket) {
		super();
		this.id = ticket.getId();
		this.title = ticket.getTitle();
		this.description = ticket.getDescription();
		this.image = ticket.getImage();
		this.asignado = ticket.getAsignado();
		this.user = ticket.getUser();
		this.status = ticket.getStatus();
		this.priority = ticket.getPriority();
		this.channel = ticket.getChannel();
		this.area = ticket.getArea();
		this.service = ticket.getService();
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public User getAsignado() {
		return asignado;
	}


	public void setAsignado(User assigned_to) {
		this.asignado = assigned_to;
	}


	public Date getSolved_at() {
		return solved_at;
	}


	public void setSolved_at(Date solved_at) {
		this.solved_at = solved_at;
	}


	public Date getClose_at() {
		return close_at;
	}


	public void setClose_at(Date close_at) {
		this.close_at = close_at;
	}


	public boolean isSla() {
		return sla;
	}


	public void setSla(boolean sla) {
		this.sla = sla;
	}


	public Character getDeleted() {
		return deleted;
	}


	public void setDeleted(Character deleted) {
		this.deleted = deleted;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public TicketType getType() {
		return type;
	}


	public void setType(TicketType type) {
		this.type = type;
	}


	public Priority getPriority() {
		return priority;
	}


	public void setPriority(Priority priority) {
		this.priority = priority;
	}


	public Channel getChannel() {
		return channel;
	}


	public void setChannel(Channel channel) {
		this.channel = channel;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}


	public Service getService() {
		return service;
	}


	public void setService(Service service) {
		this.service = service;
	}


	public Set<Comment> getComments() {
		return comments;
	}


	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}


}
