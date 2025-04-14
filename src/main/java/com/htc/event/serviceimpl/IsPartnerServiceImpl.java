package com.htc.event.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.dao.EventRepository;
import com.htc.event.dao.IsPartnerRepository;
import com.htc.event.dao.PartnerRepository;
import com.htc.event.dao.PartnerRoleRepository;
import com.htc.event.dao.UserRepository;
import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.entity.Event;
import com.htc.event.entity.IsPartner;
import com.htc.event.entity.Partner;
import com.htc.event.entity.PartnerRole;
import com.htc.event.entity.User;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.IsPartnerNotFoundException;
import com.htc.event.exception.PartnerNotFoundException;
import com.htc.event.exception.PartnerRoleNotFoundException;
import com.htc.event.mapper.IsPartnerMapper;
import com.htc.event.service.IsPartnerService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class IsPartnerServiceImpl implements IsPartnerService {

	private IsPartnerRepository isPartnerRepository;

	private EventRepository eventRepository;

	private PartnerRepository partnerRepository;

	private PartnerRoleRepository partnerRoleRepository;
	
	private UserRepository userRepository;

	private IsPartnerMapper isPartnerMapper;

	@Value("${ispartner.not.found}")
	private String isPartnerNotFoundMsg;

	@Value("${partner.not.found}")
	private String partnerNotFoundMsg;

	@Value("${partnerrole.not.found}")
	private String partnerRoleNotFoundMsg;

	@Value("${event.not.found}")
	private String eventNotFoundMsg;
//loggers for exception msg

	@Autowired
	public IsPartnerServiceImpl(IsPartnerRepository isPartnerRepository, EventRepository eventRepository,
			PartnerRepository partnerRepository, PartnerRoleRepository partnerRoleRepository,
			UserRepository userRepository, IsPartnerMapper isPartnerMapper) {
		super();
		this.isPartnerRepository = isPartnerRepository;
		this.eventRepository = eventRepository;
		this.partnerRepository = partnerRepository;
		this.partnerRoleRepository = partnerRoleRepository;
		this.userRepository = userRepository;
		this.isPartnerMapper = isPartnerMapper;
	}

	@Override
	public long countIsPartners() {
		 return isPartnerRepository.count();
	}
	   
	
	@Override
	public IsPartnerDTO saveIsPartner(IsPartnerDTO isPartnerDTO)
			throws EventNotFoundException, PartnerRoleNotFoundException, PartnerNotFoundException {
		IsPartner isPartner = new IsPartner();

		// Set event
		Event event = eventRepository.findById(isPartnerDTO.getEventId())
				.orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + isPartnerDTO.getEventId()));
		isPartner.setEvent(event);

		// Set partner
		Partner partner = partnerRepository.findById(isPartnerDTO.getPartnerId())
				.orElseThrow(() -> new PartnerNotFoundException(partnerNotFoundMsg + isPartnerDTO.getPartnerId()));
		isPartner.setPartner(partner);

		// Set partner role
		PartnerRole partnerRole = partnerRoleRepository.findById(isPartnerDTO.getPartnerRoleId()).orElseThrow(
				() -> new PartnerRoleNotFoundException(partnerRoleNotFoundMsg + isPartnerDTO.getPartnerRoleId()));
		isPartner.setPartnerRole(partnerRole);

		IsPartner savedIsPartner = isPartnerRepository.save(isPartner);
		return isPartnerMapper.toDTO(savedIsPartner);
	}

	@Override
	public Optional<IsPartnerDTO> getIsPartnerById(Integer id) throws IsPartnerNotFoundException {
		IsPartner isPartner = isPartnerRepository.findById(id)
				.orElseThrow(() -> new IsPartnerNotFoundException(isPartnerNotFoundMsg + id));
		return Optional.of(isPartnerMapper.toDTO(isPartner));
	}

	@Override
	public List<IsPartnerDTO> getAllIsPartners() {
		return isPartnerRepository.findAll().stream().map(isPartnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public IsPartnerDTO updateIsPartner(IsPartnerDTO isPartnerDTO) throws IsPartnerNotFoundException,
			EventNotFoundException, PartnerNotFoundException, PartnerRoleNotFoundException {
		IsPartner isPartner = isPartnerRepository.findById(isPartnerDTO.getId())
				.orElseThrow(() -> new IsPartnerNotFoundException(isPartnerNotFoundMsg + isPartnerDTO.getId()));

		// Update event if changed
		if (isPartnerDTO.getEventId() != null && !isPartner.getEvent().getId().equals(isPartnerDTO.getEventId())) {
			Event event = eventRepository.findById(isPartnerDTO.getEventId())
					.orElseThrow(() -> new EventNotFoundException(eventNotFoundMsg + isPartnerDTO.getEventId()));
			isPartner.setEvent(event);
		}

		// Update partner if changed
		if (isPartnerDTO.getPartnerId() != null
				&& !isPartner.getPartner().getId().equals(isPartnerDTO.getPartnerId())) {
			Partner partner = partnerRepository.findById(isPartnerDTO.getPartnerId())
					.orElseThrow(() -> new PartnerNotFoundException(partnerNotFoundMsg + isPartnerDTO.getPartnerId()));
			isPartner.setPartner(partner);
		}

		// Update partner role if changed
		if (isPartnerDTO.getPartnerRoleId() != null
				&& !isPartner.getPartnerRole().getId().equals(isPartnerDTO.getPartnerRoleId())) {
			PartnerRole partnerRole = partnerRoleRepository.findById(isPartnerDTO.getPartnerRoleId()).orElseThrow(
					() -> new PartnerRoleNotFoundException(partnerRoleNotFoundMsg + isPartnerDTO.getPartnerRoleId()));
			isPartner.setPartnerRole(partnerRole);
		}

		IsPartner updatedIsPartner = isPartnerRepository.save(isPartner);
		return isPartnerMapper.toDTO(updatedIsPartner);
	}

	@Override
	@Transactional
	public void deleteIsPartner(Integer id) throws IsPartnerNotFoundException {
		if (!isPartnerRepository.existsById(id)) {
			throw new IsPartnerNotFoundException(isPartnerNotFoundMsg + id);
		}
		isPartnerRepository.deleteById(id);
	}
	// -------------------------

	@Override
	public List<IsPartnerDTO> findPartnerAssignmentsByEvent(Integer eventId) {
		List<IsPartner> assignments = isPartnerRepository.findAllByEvent_id(eventId);
		return assignments.stream().map(isPartnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<IsPartnerDTO> findEventsByPartnerAndRole(Integer partnerId, Integer roleId) {
		List<IsPartner> assignments = isPartnerRepository.findEventsByPartnerAndRole(partnerId, roleId);
		return assignments.stream().map(isPartnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public boolean removePartnerFromEvent(Integer partnerId, Integer eventId) {
		List<IsPartner> partnerAssignments = isPartnerRepository.findAllByEvent_id(eventId);

		List<IsPartner> toRemove = partnerAssignments.stream()
				.filter(assignment -> assignment.getPartner().getId().equals(partnerId)).collect(Collectors.toList());

		if (toRemove.isEmpty()) {
			return false;
		}

		for (IsPartner assignment : toRemove) {
			isPartnerRepository.delete(assignment);
			System.out.println("deleted assignment:" + assignment);
		}

		return true;
	}
	// ---------------------------------

	@Override
	public List<IsPartnerDTO> getPartnershipsByCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAccessToPartnership(IsPartnerDTO partnership) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccessToPartner(Integer partnerId) {
		// TODO Auto-generated method stub
		return false;
	}

	 

	@Override
	public boolean isCurrentUserAdmin() {
	    // Retrieve the current user's authorities (roles)
	    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .anyMatch(role -> role.equals("ROLE_ADMIN")); // Check for 'ADMIN' role
	}

	@Override
	public List<IsPartnerDTO> filterAccessiblePartnerships(List<IsPartnerDTO> partnerships) { //m
	    // Retrieve the current user's username from the JWT
	    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(userRepository.findByUsername(currentUsername));
	    Optional<User> myuser = userRepository.findByUsername(currentUsername);
	    
	    return partnerships;
	    // Filter partnerships to include only those linked to the current user
		
//		  return partnerships.stream().map(IsPartnerDTO::getPartnerId)
//				  .map(partnerRepository::findById)
//				  .filter(Optional::isPresent)
//				  .map(Optional::get) // Extract the Partner object from the Optional
//				  .filter(partner-> partner.getUser().equals(myuser.get())).map(isPartnerMapper::toDTO).toList();
				  
//	    return partnerships.stream()
//	    	    .collect(Collectors.toMap(
//	    	        IsPartnerDTO::getPartnerId,  // Map partnerId as the key
//	    	        isPartnerDTO -> isPartnerDTO // Store the original IsPartnerDTO as the value
//	    	    )) // Create a map of partnerId to IsPartnerDTO
//	    	    .entrySet().stream() // Stream the entries of the map
//	    	    .map(entry -> {
//	    	        Optional<Partner> partnerOptional = partnerRepository.findById(entry.getKey()); // Fetch Partner by partnerId
//	    	        return partnerOptional.map(partner -> Map.entry(entry.getValue(), partner)); // Map IsPartnerDTO to Partner if present
//	    	    })
//	    	    .filter(Optional::isPresent) // Ensure the mapping is present
//	    	    .map(Optional::get) // Extract the mapping
//	    	    .filter(entry -> entry.getValue().getUser().equals(myuser.get())) // Filter based on user
//	    	    .map(Map.Entry::getKey) // Retrieve the original IsPartnerDTO
//	    	    .collect(Collectors.toList()); // Collect the filtered IsPartnerDTOs	   
	    
	}

	 

	@Override
	public List<IsPartnerDTO> findPartnershipsByPartner(Integer partnerId)  {
		List<IsPartner> assignments = isPartnerRepository.findAllByPartner_id(partnerId);
		return assignments.stream().map(isPartnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<IsPartnerDTO> findPartnershipsByRole(Integer roleId) {
		List<IsPartner> assignments = isPartnerRepository.findAllByPartnerRole_id(roleId);
		return assignments.stream().map(isPartnerMapper::toDTO).collect(Collectors.toList());
	}

}
