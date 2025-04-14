package com.htc.event.entity;

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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "event_type_ss") //@Data
@RequiredArgsConstructor @ToString
@AllArgsConstructor @Getter @Setter  
public class EventType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
    @Column(name = "type_name", length = 255, nullable = false)
    private String typeName;
    
    @JsonIgnore
    @OneToMany(mappedBy = "eventType", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Event> events = new HashSet<>();

	 

 	
	
    
     
}
