package com.htc.event.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.dto.EventTypeDTO;
import com.htc.event.mapper.EventTypeMapper;
import com.htc.event.entity.Event;
import com.htc.event.entity.Event.EventStatus;
import com.htc.event.entity.EventType;
import com.htc.event.exception.EventTypeNotFoundException;
import com.htc.event.dao.EventTypeRepository;
import com.htc.event.service.EventTypeService;

@Service
@Transactional
public class EventTypeServiceImpl implements EventTypeService {

	private EventTypeRepository eventTypeRepository;

	private EventTypeMapper eventTypeMapper;

	@Value("${eventtype.not.found}")
	private String eventTypeNotFoundMsg;

	@Autowired
	public EventTypeServiceImpl(EventTypeRepository eventTypeRepository, EventTypeMapper eventTypeMapper) {
		super();
		this.eventTypeRepository = eventTypeRepository;
		this.eventTypeMapper = eventTypeMapper;
	}

	@Override
	public long countEventsTypes() {
		return eventTypeRepository.count();
	}
	
	@Override
	public EventTypeDTO saveEventType(EventTypeDTO eventTypeDTO) {
		EventType eventType = new EventType();
		eventType.setTypeName(eventTypeDTO.getTypeName());

		EventType savedEventType = eventTypeRepository.save(eventType);
		return eventTypeMapper.toDTO(savedEventType);
	}

	@Override
	public Optional<EventTypeDTO> getEventTypeById(Integer id) throws EventTypeNotFoundException {
		EventType eventType = eventTypeRepository.findById(id)
				.orElseThrow(() -> new EventTypeNotFoundException(eventTypeNotFoundMsg + id));
		return Optional.of(eventTypeMapper.toDTO(eventType));
	}

	@Override
	public List<EventTypeDTO> getAllEventTypes() {
		return eventTypeRepository.findAll().stream().map(eventTypeMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public EventTypeDTO updateEventType(EventTypeDTO eventTypeDTO) throws EventTypeNotFoundException {
		EventType eventType = eventTypeRepository.findById(eventTypeDTO.getId())
				.orElseThrow(() -> new EventTypeNotFoundException(eventTypeNotFoundMsg + eventTypeDTO.getId()));

		eventType.setTypeName(eventTypeDTO.getTypeName());

		EventType updatedEventType = eventTypeRepository.save(eventType);
		return eventTypeMapper.toDTO(updatedEventType);
	}

	@Override
	public void deleteEventType(Integer id) throws EventTypeNotFoundException {
		if (!eventTypeRepository.existsById(id)) {
			throw new EventTypeNotFoundException(eventTypeNotFoundMsg + id);
		}
		eventTypeRepository.deleteById(id);
	}

//---------------------------------------------------

	@Override
	public List<EventTypeDTO> findEventTypesWithEventStatus(EventStatus status) {
		// Fetch event types that are associated with events of a specific status
		List<EventType> eventTypes = eventTypeRepository.findEventTypesWithEventStatus(status);

		// Convert the event types to DTOs before returning
		return eventTypes.stream().map(eventTypeMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Map<String, Long> getEventTypesByPopularity() {
		List<Object[]> results = eventTypeRepository.findEventTypesByPopularity();
		Map<String, Long> popularityMap = new HashMap<>();

		for (Object[] result : results) {
			EventType type = (EventType) result[0];
			Long count = (Long) result[1];
			popularityMap.put(type.getTypeName(), count);
		}

		return popularityMap;
	}
}
