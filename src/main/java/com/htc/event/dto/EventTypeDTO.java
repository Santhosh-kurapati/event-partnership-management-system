package com.htc.event.dto;

import java.util.HashSet;
import java.util.Set;

import com.htc.event.entity.IsPartner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeDTO {
    
    
    private Integer id;
    
    @NotNull(message = "Event name cannot be null")
    @NotBlank(message = "Event name cannot be empty or just whitespace")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String typeName;
    
     

}
