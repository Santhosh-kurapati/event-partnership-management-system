package com.htc.event.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRoleDTO {
    private Integer id;
    
    @NotNull(message = "Partner role name cannot be null")
    @Size(min = 3, max = 100, message = "Partner role name must be between 3 and 100 characters")
    private String roleName;

   
	 
}
