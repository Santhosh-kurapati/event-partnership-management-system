package com.htc.event.service;

import java.util.List;
import java.util.Optional;

import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.IsPartnerNotFoundException;
import com.htc.event.exception.PartnerNotFoundException;
import com.htc.event.exception.PartnerRoleNotFoundException;

public interface IsPartnerService {

	// Get partnerships associated with the current authenticated user's partner
	List<IsPartnerDTO> getPartnershipsByCurrentUser();

	// Security methods
	boolean hasAccessToPartnership(IsPartnerDTO partnership);

	boolean hasAccessToPartner(Integer partnerId);

	boolean isCurrentUserAdmin();

	
	//normal methods
	boolean removePartnerFromEvent(Integer partnerId, Integer eventId);

	List<IsPartnerDTO> findEventsByPartnerAndRole(Integer partnerId, Integer roleId);

	List<IsPartnerDTO> findPartnerAssignmentsByEvent(Integer eventId);

	void deleteIsPartner(Integer id) throws IsPartnerNotFoundException;

	IsPartnerDTO updateIsPartner(IsPartnerDTO isPartnerDTO) throws IsPartnerNotFoundException, EventNotFoundException,
			PartnerNotFoundException, PartnerRoleNotFoundException;

	IsPartnerDTO saveIsPartner(IsPartnerDTO isPartnerDTO)
			throws EventNotFoundException, PartnerRoleNotFoundException, PartnerNotFoundException;

	Optional<IsPartnerDTO> getIsPartnerById(Integer id) throws IsPartnerNotFoundException;

	List<IsPartnerDTO> getAllIsPartners();

	List<IsPartnerDTO> filterAccessiblePartnerships(List<IsPartnerDTO> partnerships);

	List<IsPartnerDTO> findPartnershipsByPartner(Integer partnerId);

	List<IsPartnerDTO> findPartnershipsByRole(Integer roleId);

	long countIsPartners();

}
