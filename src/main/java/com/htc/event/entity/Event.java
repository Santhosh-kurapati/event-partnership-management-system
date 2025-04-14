package com.htc.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_ss")    
@AllArgsConstructor 
@NoArgsConstructor
@Getter @Setter @ToString
public class Event {
    
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "event_name", length = 255, nullable = false)
    private String eventName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    
    @Column(name = "event_location", length = 255)
    private String eventLocation;
    
    @Column(name = "event_description", columnDefinition = "TEXT")
    private String eventDescription;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;
    
    @JsonIgnore
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<IsPartner> eventPartners = new HashSet<>();
    
    public enum EventStatus {
        DRAFT, 
        PUBLISHED, 
        ACTIVE, 
        COMPLETED, 
        CANCELLED
    }

	public Event(String eventName, String eventLocation, String eventDescription,
			LocalDateTime startTime, LocalDateTime endTime, EventStatus eventStatus) {
		super();
		this.eventName = eventName;
		this.eventLocation = eventLocation;
		this.eventDescription = eventDescription;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventStatus = eventStatus;
 	}
    
 
    
}
