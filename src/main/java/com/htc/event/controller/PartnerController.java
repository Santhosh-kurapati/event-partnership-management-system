package com.htc.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.htc.event.dto.PartnerDTO;
import com.htc.event.entity.Partner;
import com.htc.event.exception.PartnerNotFoundException;
import com.htc.event.security.UserDetailsImpl;
import com.htc.event.service.MyCustomUserDetailService;
import com.htc.event.service.PartnerService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

	@Autowired
	private PartnerService partnerService;

	// Public endpoints (no authentication required)
	@GetMapping("/public/all")
	public ResponseEntity<List<PartnerDTO>> getAllPublicPartners() {
		List<PartnerDTO> partners = partnerService.getAllPartners();
		return new ResponseEntity<>(partners, HttpStatus.OK);
	}

	@GetMapping("/public/{id}")
	public ResponseEntity<PartnerDTO> getPublicPartnerById(@PathVariable("id") Integer id)
			throws PartnerNotFoundException {
		Optional<PartnerDTO> partner = partnerService.getPartnerById(id);
		return partner.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// get total partners count
	@GetMapping("/public/count") // fetch the published events in next 3 months
	public ResponseEntity<Long> countTotalEvents() {
		long totalpartners = partnerService.countPartners();
		return new ResponseEntity<>(totalpartners, HttpStatus.OK);
	}

	// Admin-only endpoints
//	@PostMapping("/admin/create")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<PartnerDTO> createPartner(@Valid @RequestBody PartnerDTO partner) {
//		PartnerDTO savedPartner = partnerService.savePartner(partner);
//		return new ResponseEntity<>(savedPartner, HttpStatus.CREATED);
//	}

	@PostMapping("/rep/create") //
	@PreAuthorize("hasRole('PARTNER_REP')")
	public ResponseEntity<?> createPartner(@RequestBody PartnerDTO partnerDTO) {
		// Extract the authenticated user ID
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Integer userId = userDetails.getId(); // Assuming CustomUserDetails contains userId

		// Set the userId in PartnerDTO
		partnerDTO.setUserId(userId);

		// Pass PartnerDTO to the service layer to save to DB
		PartnerDTO savedPartner = partnerService.savePartner(partnerDTO);
		return new ResponseEntity<>(savedPartner, HttpStatus.CREATED);
	}

	@PutMapping("/admin/{id}") //
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PartnerDTO> updatePartner(@PathVariable("id") Integer id,
			@Valid @RequestBody PartnerDTO partner) throws PartnerNotFoundException {
		if (!partnerService.getPartnerById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		partner.setId(id);
		PartnerDTO updatedPartner = partnerService.updatePartner(partner);
		return new ResponseEntity<>(updatedPartner, HttpStatus.OK);
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletePartner(@PathVariable("id") Integer id) throws PartnerNotFoundException {
		if (!partnerService.getPartnerById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		partnerService.deletePartner(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Partner Rep specific endpoints
	@GetMapping("/rep/details")
	@PreAuthorize("hasRole('PARTNER_REP')")
	public ResponseEntity<PartnerDTO> getPartnerByRepresentative() {
		// Get currently authenticated user's partner
		PartnerDTO partner = partnerService.getPartnerByCurrentUser();
		if (partner != null) {
			return new ResponseEntity<>(partner, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/rep/update")
	@PreAuthorize("hasRole('PARTNER_REP')")
	public ResponseEntity<PartnerDTO> updatePartnerDetails(@Valid @RequestBody PartnerDTO partnerDetails)
			throws PartnerNotFoundException {
		PartnerDTO partner = partnerService.getPartnerByCurrentUser();
		if (partner == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Only allow updating certain fields
		partner.setPartnerDetails(partnerDetails.getPartnerDetails());
		// You might want to restrict updating the name or other sensitive fields

		PartnerDTO updatedPartner = partnerService.updatePartner(partner);
		return new ResponseEntity<>(updatedPartner, HttpStatus.OK);
	}

	// Both Admin and Partner Rep can access
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<PartnerDTO>> getAllPartners() {
		List<PartnerDTO> partners = partnerService.getAllPartners();
		return new ResponseEntity<>(partners, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<PartnerDTO> getPartnerById(@PathVariable("id") Integer id) throws PartnerNotFoundException {
		Optional<PartnerDTO> partner = partnerService.getPartnerById(id);
		return partner.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/byName")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<PartnerDTO>> getPartnersByNameContaining(@RequestParam("name") String name) {
		List<PartnerDTO> partners = partnerService.findPartnersByName(name);
		return new ResponseEntity<>(partners, HttpStatus.OK);
	}
}