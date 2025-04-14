package com.htc.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.htc.event.dto.PartnerRoleDTO;
import com.htc.event.entity.PartnerRole;
import com.htc.event.exception.PartnerRoleNotFoundException;
import com.htc.event.service.PartnerRoleService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partnerroles")
public class PartnerRoleController {

	@Autowired
	private PartnerRoleService partnerRoleService;

	// Public endpoints
	@GetMapping("/public/all")
	public ResponseEntity<List<PartnerRoleDTO>> findAllPublicPartnerRoles() {
		List<PartnerRoleDTO> partnerRoles = partnerRoleService.getAllPartnerRoles();
		return new ResponseEntity<>(partnerRoles, HttpStatus.OK);
	}

	// get total partner roles count
	@GetMapping("/public/count") // fetch the published events in next 3 months
	public ResponseEntity<Long> countTotalEvents() {
		long totalPartnerRoles = partnerRoleService.countPartnerRoles();
		return new ResponseEntity<>(totalPartnerRoles, HttpStatus.OK);
	}

	// Admin-only endpoints
	@PostMapping("/admin/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PartnerRoleDTO> createPartnerRole(@Valid @RequestBody PartnerRoleDTO partnerRole) {
		PartnerRoleDTO savedPartnerRole = partnerRoleService.savePartnerRole(partnerRole);
		return new ResponseEntity<>(savedPartnerRole, HttpStatus.CREATED);
	}

	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PartnerRoleDTO> updatePartnerRole(@PathVariable("id") Integer id,
			@Valid @RequestBody PartnerRoleDTO partnerRole) throws PartnerRoleNotFoundException {
		if (!partnerRoleService.getPartnerRoleById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		partnerRole.setId(id);
		PartnerRoleDTO updatedPartnerRole = partnerRoleService.updatePartnerRole(partnerRole);
		return new ResponseEntity<>(updatedPartnerRole, HttpStatus.OK);
	}

	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletePartnerRole(@PathVariable("id") Integer id) throws PartnerRoleNotFoundException {
		if (!partnerRoleService.getPartnerRoleById(id).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		partnerRoleService.deletePartnerRole(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// Both Admin and Partner Rep can access
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<List<PartnerRoleDTO>> findAllPartnerRoles() {
		List<PartnerRoleDTO> partnerRoles = partnerRoleService.getAllPartnerRoles();
		return new ResponseEntity<>(partnerRoles, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('PARTNER_REP')")
	public ResponseEntity<PartnerRoleDTO> findPartnerRoleById(@PathVariable("id") Integer id)
			throws PartnerRoleNotFoundException {
		Optional<PartnerRoleDTO> partnerRole = partnerRoleService.getPartnerRoleById(id);
		return partnerRole.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
