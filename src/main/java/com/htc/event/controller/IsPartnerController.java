package com.htc.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.IsPartnerNotFoundException;
import com.htc.event.exception.PartnerNotFoundException;
import com.htc.event.exception.PartnerRoleNotFoundException;
import com.htc.event.service.IsPartnerService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partnerships")
public class IsPartnerController {

	@Autowired
	private IsPartnerService isPartnerService;

	// get total partners count
		@GetMapping("/public/count") 
		public ResponseEntity<Long> countTotalEvents() {
			long totalPartnerships = isPartnerService.countIsPartners();
			return new ResponseEntity<>(totalPartnerships, HttpStatus.OK);
		}
	
	// Admin-only endpoints
	@PostMapping("/admin/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<IsPartnerDTO> createPartnership(@Valid @RequestBody IsPartnerDTO isPartner)
			throws EventNotFoundException, PartnerRoleNotFoundException, PartnerNotFoundException {
		IsPartnerDTO savedPartnership = isPartnerService.saveIsPartner(isPartner);
		return new ResponseEntity<>(savedPartnership, HttpStatus.CREATED);
	}

	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<IsPartnerDTO> updatePartnership(@PathVariable("id") Integer id,
			@Valid @RequestBody IsPartnerDTO isPartner) throws EventNotFoundException, IsPartnerNotFoundException,
			PartnerNotFoundException, PartnerRoleNotFoundException {
		if (!isPartnerService.getIsPartnerById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		isPartner.setId(id);
		IsPartnerDTO updatedPartnership = isPartnerService.updateIsPartner(isPartner);
		return new ResponseEntity<>(updatedPartnership, HttpStatus.OK);
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletePartnership(@PathVariable("id") Integer id) throws IsPartnerNotFoundException {
		if (!isPartnerService.getIsPartnerById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		isPartnerService.deleteIsPartner(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	

	// Partner Rep specific endpoints - can only view partnerships for their
	// organization
	@GetMapping("/rep/mypartnerships")//m
	@PreAuthorize("hasRole('PARTNER_REP')")
	public ResponseEntity<List<IsPartnerDTO>> getMyPartnerships() {
		List<IsPartnerDTO> partnerships = isPartnerService.getPartnershipsByCurrentUser();
		return new ResponseEntity<>(partnerships, HttpStatus.OK);
	}

	 
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<IsPartnerDTO>> getAllPartnerships() {
		List<IsPartnerDTO> partnerships = isPartnerService.getAllIsPartners();
		return new ResponseEntity<>(partnerships, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')") //m
	public ResponseEntity<IsPartnerDTO> getPartnershipById(@PathVariable("id") Integer id) throws IsPartnerNotFoundException {
		Optional<IsPartnerDTO> partnership = isPartnerService.getIsPartnerById(id);

		// For PARTNER_REP, check if they have access to this partnership
		if (partnership.isPresent() && isPartnerService.hasAccessToPartnership(partnership.get())) {
			return new ResponseEntity<>(partnership.get(), HttpStatus.OK);
		} else if (!partnership.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/byEvent/{eventId}")  
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<IsPartnerDTO>> getPartnershipsByEvent(@PathVariable("eventId") Integer eventId) {
		List<IsPartnerDTO> partnerships = isPartnerService.findPartnerAssignmentsByEvent(eventId);

		 //For PARTNER_REP, filter to only include partnerships they have access to
		if (!isPartnerService.isCurrentUserAdmin()) {
			partnerships = isPartnerService.filterAccessiblePartnerships(partnerships);
			 
		}

		return new ResponseEntity<>(partnerships, HttpStatus.OK);
	}

	@GetMapping("/byPartner/{partnerId}")  
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<IsPartnerDTO>> getPartnershipsByPartner(@PathVariable("partnerId") Integer partnerId) {
		// For PARTNER_REP, check if they have access to this partner
//		if (!isPartnerService.isCurrentUserAdmin() && !isPartnerService.hasAccessToPartner(partnerId)) {
//			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//		}

		List<IsPartnerDTO> partnerships = isPartnerService.findPartnershipsByPartner(partnerId);
		return new ResponseEntity<>(partnerships, HttpStatus.OK);
	}

	@GetMapping("/byRole/{roleId}")   
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<IsPartnerDTO>> getPartnershipsByRole(@PathVariable("roleId") Integer roleId) {
		List<IsPartnerDTO> partnerships = isPartnerService.findPartnershipsByRole(roleId);

		// For PARTNER_REP, filter to only include partnerships they have access to
//		if (!isPartnerService.isCurrentUserAdmin()) {
//			partnerships = isPartnerService.filterAccessiblePartnerships(partnerships);
//		}

		return new ResponseEntity<>(partnerships, HttpStatus.OK);
	}
}
