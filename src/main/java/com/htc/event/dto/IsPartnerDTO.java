package com.htc.event.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsPartnerDTO {

    // id is auto-generated, no validation required
    private Integer id;

    @NotNull(message = "Event ID cannot be null")
    @Min(value = 101, message = "Event ID must be greater than 100")
    private Integer eventId;

    @NotNull(message = "Partner ID cannot be null")
    @Min(value = 401, message = "Partner ID must be greater than 400")
    private Integer partnerId;

    @NotNull(message = "Partner Role ID cannot be null")
    @Min(value = 301, message = "Partner Role ID must be greater than 300")
    private Integer partnerRoleId;
}
