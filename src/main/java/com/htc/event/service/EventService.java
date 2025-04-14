package com.htc.event.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.htc.event.dto.EventDTO;
import com.htc.event.entity.Event;
import com.htc.event.entity.Event.EventStatus;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.IsPartnerNotFoundException;

public interface EventService {
	List<EventDTO> getAllEvents();

	Optional<EventDTO> getEventById(Integer id);

    EventDTO saveEvent(EventDTO eventDTO);

	void deleteEvent(Integer id) throws EventNotFoundException ;

	List<EventDTO> findEventsByPartnerRole(String roleName);

	List<EventDTO> findUpcomingEvents();

	List<EventDTO> findEventsByType(Integer eventTypeId);

	boolean updateEventStatus(Integer eventId, Event.EventStatus newStatus);

	List<EventDTO> findEventsByStatusAndDateRange(EventStatus status, LocalDate startDate, LocalDate endDate);

	EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException;

	long countEvents();

	List<EventDTO> getEventByName(String name);
}
