package com.htc.event.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.htc.event.dto.EventTypeDTO;
import com.htc.event.entity.Event;
import com.htc.event.entity.EventType;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.EventTypeNotFoundException;

public interface EventTypeService {
    List<EventTypeDTO> getAllEventTypes();
    Optional<EventTypeDTO> getEventTypeById(Integer id) throws EventTypeNotFoundException;
    void deleteEventType(Integer id) throws EventTypeNotFoundException;
    List<EventTypeDTO> findEventTypesWithEventStatus(Event.EventStatus status);
    Map<String, Long> getEventTypesByPopularity();
	EventTypeDTO saveEventType(EventTypeDTO eventTypeDTO);
	EventTypeDTO updateEventType(EventTypeDTO eventTypeDTO) throws EventTypeNotFoundException;
	long countEventsTypes();
}
