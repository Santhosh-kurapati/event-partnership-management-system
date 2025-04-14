package com.htc.event.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htc.event.entity.Partner;

@Entity
@Table(name = "user_ss")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor  
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 50, unique = true)
    private String username;
    
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    //   link to partner if user is a partner representative
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Partner partner;
    
    public enum UserRole {
        ROLE_ADMIN,           // Can create/manage events
        ROLE_PARTNER_REP      // Represents a partner organization
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", role="
				+ role + ", partner=" + partner.getPartnerName() + "]";
	}
    
    
    
}