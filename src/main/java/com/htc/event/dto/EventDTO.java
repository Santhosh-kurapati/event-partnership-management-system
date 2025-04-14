package com.htc.event.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.htc.event.entity.Event.EventStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    
     
    private Integer id;

    @NotNull(message = "Event name cannot be null")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String eventName;

    @NotNull(message = "Event type ID cannot be null")
    @Min(value = 201, message = "Event ID must be greater than 200")
    private Integer eventTypeId;

    @NotNull(message = "Event location cannot be null")
    @Size(min = 3, max = 255, message = "Event location must be between 3 and 255 characters")
    private String eventLocation;

    @NotNull(message = "Event description cannot be null")
    @Size(min = 3, max = 500, message = "Event description must be between 3 and 500 characters")
    private String eventDescription;

    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time must be in the future or present")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Event status cannot be null")
    private EventStatus eventStatus;

     
}
