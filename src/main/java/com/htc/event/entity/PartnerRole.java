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
@Table(name = "partner_role_ss")  //@Data
@RequiredArgsConstructor
@AllArgsConstructor @Getter @Setter @ToString
public class PartnerRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "role_name", length = 255, nullable = false)
    private String roleName;
    
    @JsonIgnore
    @OneToMany(mappedBy = "partnerRole", cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<IsPartner> roleAssignments = new HashSet<>();

	 

}
