package com.htc.event.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.dto.EventDTO;
import com.htc.event.mapper.EventMapper;
import com.htc.event.entity.Event;
import com.htc.event.entity.Event.EventStatus;
import com.htc.event.entity.EventType;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.dao.EventRepository;
import com.htc.event.dao.EventTypeRepository;
import com.htc.event.service.EventService;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private EventRepository eventRepository;

	private EventTypeRepository eventTypeRepository;

	private EventMapper eventMapper;

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, EventTypeRepository eventTypeRepository,
			EventMapper eventMapper) {
		super();
		this.eventRepository = eventRepository;
		this.eventTypeRepository = eventTypeRepository;
		this.eventMapper = eventMapper;
	}

	@Value("${event.not.found}")
	private String eventNotFoundMsg;

	@Override
	public EventDTO saveEvent(EventDTO eventDTO) {
		Event event = new Event(eventDTO.getEventName(), eventDTO.getEventLocation(), eventDTO.getEventDescription(),
				eventDTO.getStartTime(), eventDTO.getEndTime(), eventDTO.getEventStatus());

		// Set event type
		EventType eventType = eventTypeRepository.findById(eventDTO.getEventTypeId())
				.orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + eventDTO.getEventTypeId()));
		event.setEventType(eventType);

		Event savedEvent = eventRepository.save(event);
		return eventMapper.toDTO(savedEvent);
	}

	@Override
	public Optional<EventDTO> getEventById(Integer id) {
		Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + id));
		return Optional.of(eventMapper.toDTO(event));
	}

	@Override
	public List<EventDTO> getAllEvents() {
		return eventRepository.findAll().stream().map(eventMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public EventDTO updateEvent(EventDTO eventDTO) throws EventNotFoundException {
		Event event = eventRepository.findById(eventDTO.getId())
				.orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + eventDTO.getId()));

		event.setEventName(eventDTO.getEventName());
		event.setEventLocation(eventDTO.getEventLocation());
		event.setEventDescription(eventDTO.getEventDescription());
		event.setStartTime(eventDTO.getStartTime());
		event.setEndTime(eventDTO.getEndTime());
		event.setEventStatus(eventDTO.getEventStatus());

		// Update event type if changed
		if (eventDTO.getEventTypeId() != null
				&& (event.getEventType() == null || !event.getEventType().getId().equals(eventDTO.getEventTypeId()))) {
			EventType eventType = eventTypeRepository.findById(eventDTO.getEventTypeId())
					.orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + eventDTO.getEventTypeId()));
			event.setEventType(eventType);
		}

		Event updatedEvent = eventRepository.save(event);
		return eventMapper.toDTO(updatedEvent);
	}

	@Override
	public void deleteEvent(Integer id) throws EventNotFoundException {
		if (!eventRepository.existsById(id)) {
			throw new EventNotFoundException(eventNotFoundMsg + id);
		}
		eventRepository.deleteById(id);
	}

	@Override
	public long countEvents() {
		return eventRepository.count();
	}

	// ---------------------------------------------------
	@Override
	public List<EventDTO> getEventByName(String name) {
		return eventRepository.findByEventName(name).stream().map(eventMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> findEventsByStatusAndDateRange(EventStatus status, LocalDate startDate,
			LocalDate endDate) {
		List<Event> events = eventRepository.findEventsByStatusAndDateRange(status, startDate, endDate);
		return events.stream().map(eventMapper::toDTO) // Convert each Event to EventDTO
				.collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> findEventsByPartnerRole(String roleName) {
		List<Event> events = eventRepository.findEventsByPartnerRole(roleName);
		return events.stream().map(eventMapper::toDTO) // Convert each Event to EventDTO
				.collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> findUpcomingEvents() {
		LocalDate now = LocalDate.now();
		// Find events that start in the future and are either PUBLISHED or ACTIVE
		List<Event> events = eventRepository.findEventsByStatusAndDateRange(EventStatus.PUBLISHED, now,
				now.plusMonths(3));
		return events.stream().map(eventMapper::toDTO) // Convert each Event to EventDTO
				.collect(Collectors.toList());
	}

	@Override
	public List<EventDTO> findEventsByType(Integer eventTypeId) {
		Optional<EventType> eventType = eventTypeRepository.findById(eventTypeId);
		if (eventType.isPresent()) {
			List<Event> events = eventType.get().getEvents().stream().collect(Collectors.toList());
			return events.stream().map(eventMapper::toDTO) // Convert each Event to EventDTO
					.collect(Collectors.toList());
		}
		return List.of();
	}

	@Override
	@Transactional
	public boolean updateEventStatus(Integer eventId, EventStatus newStatus) {
		Optional<Event> eventOpt = eventRepository.findById(eventId);
		if (eventOpt.isPresent()) {
			Event event = eventOpt.get();
			event.setEventStatus(newStatus);
			eventRepository.save(event);
			return true;
		}
		return false;
	}

}
