package com.htc.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.htc.event.dto.EventDTO;
import com.htc.event.entity.Event.EventStatus;
import com.htc.event.service.EventService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@Autowired
	private EventService eventService;

	// Public endpoints (no authentication required)
	@GetMapping("/public/upcoming")   //fetch the published events in next 3 months
	public ResponseEntity<List<EventDTO>> getUpcomingEvents() {
		List<EventDTO> events = eventService.findUpcomingEvents();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}
	
	//get total events count
	@GetMapping("/public/count")   //fetch the published events in next 3 months
	public ResponseEntity<Long> countTotalEvents() {
		long totalevents = eventService.countEvents();
		return new ResponseEntity<>(totalevents, HttpStatus.OK);
	}

	@GetMapping("/public/{id}")
	public ResponseEntity<EventDTO> getPublicEventById(@PathVariable("id") Integer id) {
		Optional<EventDTO> event = eventService.getEventById(id);
		if (event.isPresent() && event.get().getEventStatus() == EventStatus.PUBLISHED) {
			return new ResponseEntity<>(event.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// Admin-only endpoints
	@PostMapping("/admin/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO event) {
		EventDTO savedEvent = eventService.saveEvent(event);
		return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
	}

	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") Integer id, @Valid @RequestBody EventDTO event) {
		if (!eventService.getEventById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		event.setId(id);
		EventDTO updatedEvent = eventService.updateEvent(event);
		return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer id) {
		if (!eventService.getEventById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		eventService.deleteEvent(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PatchMapping("/admin/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateEventStatus(@PathVariable("id") Integer id, @RequestParam("eventstatus") EventStatus status) {

		boolean updated = eventService.updateEventStatus(id, status);
		if (updated) {
			return new ResponseEntity<>("Event status updated successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
	}

	// Both Admin and Partner Rep can access
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<EventDTO>> getAllEvents() {
		List<EventDTO> events = eventService.getAllEvents();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/{Id}")
	@PreAuthorize("hasRole('PARTNER_REP')")
	public ResponseEntity<EventDTO> getEventById(@PathVariable("Id") Integer id) {
		Optional<EventDTO> event = eventService.getEventById(id);
		return event.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/byType/{typeId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<EventDTO>> getEventsByType(@PathVariable("typeId") Integer typeId) {
		List<EventDTO> events = eventService.findEventsByType(typeId);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/byStatusAndDate")//
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<EventDTO>> getEventsByStatusAndDateRange(@RequestParam("status") EventStatus status,
			@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {

		List<EventDTO> events = eventService.findEventsByStatusAndDateRange(status, startDate, endDate);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	@GetMapping("/byPartnerRole")//
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<EventDTO>> getEventsByPartnerRole(@RequestParam("roleName") String roleName) {
		List<EventDTO> events = eventService.findEventsByPartnerRole(roleName);
		return new ResponseEntity<>(events, HttpStatus.OK);
	}
}
