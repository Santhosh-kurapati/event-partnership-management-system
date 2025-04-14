package com.htc.event.dto;

import java.util.HashSet;
import java.util.Set;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {

    // id is auto-generated, so no validation required
    private Integer id;

    @NotNull(message = "Partner name cannot be null")
    @Size(min = 3, max = 100, message = "Partner name must be between 3 and 100 characters")
    private String partnerName;

    @Size(max = 255, message = "Partner details must be less than or equal to 255 characters")
    private String partnerDetails;
    
    private Integer userId;

}

