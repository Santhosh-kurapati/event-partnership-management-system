package com.htc.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "partner_ss") // @Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter @ToString
public class Partner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "partner_name", length = 255, nullable = false)
	private String partnerName;

	@Column(name = "partner_details", columnDefinition = "TEXT")
	private String partnerDetails;

	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "partner", cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<IsPartner> partnerEvents = new HashSet<>();

	public Partner(String partnerName, String partnerDetails, Set<IsPartner> partnerEvents) {
		super();
		this.partnerName = partnerName;
		this.partnerDetails = partnerDetails;
		this.partnerEvents = partnerEvents; 
	}

}