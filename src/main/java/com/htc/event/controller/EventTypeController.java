package com.htc.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.htc.event.dto.EventTypeDTO;
import com.htc.event.entity.EventType;
import com.htc.event.exception.EventTypeNotFoundException;
import com.htc.event.service.EventTypeService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventtypes")
public class EventTypeController {

	@Autowired
	private EventTypeService eventTypeService;

	// Public endpoints (no authentication required)
	@GetMapping("/public/all")
	public ResponseEntity<List<EventTypeDTO>> getAllPublicEventTypes() {
		List<EventTypeDTO> eventTypes = eventTypeService.getAllEventTypes();
		return new ResponseEntity<>(eventTypes, HttpStatus.OK);
	}

	// get total event types count
	@GetMapping("/public/count") // fetch the published events in next 3 months
	public ResponseEntity<Long> countTotalEventTypes() {
		long totalEventTypes = eventTypeService.countEventsTypes();
		return new ResponseEntity<>(totalEventTypes, HttpStatus.OK);
	}

	// Admin-only endpoints
	@PostMapping("/admin/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventTypeDTO> createEventType(@Valid @RequestBody EventTypeDTO eventType) {
		EventTypeDTO savedEventType = eventTypeService.saveEventType(eventType);
		return new ResponseEntity<>(savedEventType, HttpStatus.CREATED);
	}

	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventTypeDTO> updateEventType(@PathVariable("id") Integer id,
			@Valid @RequestBody EventTypeDTO eventType) throws EventTypeNotFoundException {
		if (!eventTypeService.getEventTypeById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		eventType.setId(id);
		EventTypeDTO updatedEventType = eventTypeService.updateEventType(eventType);
		return new ResponseEntity<>(updatedEventType, HttpStatus.OK);
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteEventType(@PathVariable("id") Integer id) throws EventTypeNotFoundException {
		if (!eventTypeService.getEventTypeById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		eventTypeService.deleteEventType(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Both Admin and Partner Rep can access
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<EventTypeDTO>> getAllEventTypes() {
		List<EventTypeDTO> eventTypes = eventTypeService.getAllEventTypes();
		return new ResponseEntity<>(eventTypes, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','PARTNER_REP')")
	public ResponseEntity<EventTypeDTO> getEventTypeById(@PathVariable("id") Integer id)
			throws EventTypeNotFoundException {
		Optional<EventTypeDTO> eventType = eventTypeService.getEventTypeById(id);
		return eventType.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}