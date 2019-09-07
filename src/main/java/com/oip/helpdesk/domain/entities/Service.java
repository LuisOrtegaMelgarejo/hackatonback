package com.oip.helpdesk.domain.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "service")
public class Service {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "name")
    private String name;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "configuration_item_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ConfigurationItem configurationItem;

	@OneToMany(mappedBy="service", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Ticket> tickets = new ArrayList<>();

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

	public ConfigurationItem getConfigurationItem() {
		return configurationItem;
	}

	public void setConfigurationItem(ConfigurationItem configurationItem) {
		this.configurationItem = configurationItem;
	}
}
