package com.htc.event.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.htc.event.entity.Event;
import com.htc.event.entity.IsPartner;
import com.htc.event.entity.Partner;
import com.htc.event.entity.PartnerRole;
import com.htc.event.entity.User;
import com.htc.event.exception.EventNotFoundException;
import com.htc.event.exception.PartnerNotFoundException;
import com.htc.event.mapper.PartnerMapper;
import com.htc.event.dao.EventRepository;
import com.htc.event.dao.IsPartnerRepository;
import com.htc.event.dao.PartnerRepository;
import com.htc.event.dao.PartnerRoleRepository;
import com.htc.event.dao.UserRepository;
import com.htc.event.dto.PartnerDTO;
import com.htc.event.security.UserDetailsImpl;
import com.htc.event.service.PartnerService;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PartnerServiceImpl implements PartnerService {

	private PartnerRepository partnerRepository;

	private EventRepository eventRepository;

	private PartnerRoleRepository partnerRoleRepository;

	private IsPartnerRepository isPartnerRepository;

	private PartnerMapper partnerMapper;

	private UserRepository userRepository;

	@Autowired
	public PartnerServiceImpl(PartnerRepository partnerRepository, EventRepository eventRepository,
			PartnerRoleRepository partnerRoleRepository, IsPartnerRepository isPartnerRepository,
			PartnerMapper partnerMapper, UserRepository userRepository) {
		this.partnerRepository = partnerRepository;
		this.eventRepository = eventRepository;
		this.partnerRoleRepository = partnerRoleRepository;
		this.isPartnerRepository = isPartnerRepository;
		this.partnerMapper = partnerMapper;
		this.userRepository = userRepository;
	}

	@Value("${partner.not.found}")
	private String partnerNotFoundMsg;

	@Override
	public List<PartnerDTO> getAllPartners() {
		return partnerRepository.findAll().stream().map(partnerMapper::toDTO).toList();
	}

	@Override
	public Optional<PartnerDTO> getPartnerById(Integer id) throws PartnerNotFoundException {
		Partner partner = partnerRepository.findById(id)
				.orElseThrow(() -> new PartnerNotFoundException(partnerNotFoundMsg + id));
		return Optional.of(partnerMapper.toDTO(partner));
	}

	@Override
	public PartnerDTO savePartner(PartnerDTO partnerDTO) {
		Partner partner = new Partner();
		partner.setPartnerName(partnerDTO.getPartnerName());
		partner.setPartnerDetails(partnerDTO.getPartnerDetails());

		Partner savedPartner = partnerRepository.save(partner);
		return partnerMapper.toDTO(savedPartner);
	}

	@Override
	public PartnerDTO updatePartner(PartnerDTO partnerDTO) throws PartnerNotFoundException {
		Partner partner = partnerRepository.findById(partnerDTO.getId())
				.orElseThrow(() -> new PartnerNotFoundException("Partner not found for id: " + partnerDTO.getId()));

		partner.setPartnerName(partnerDTO.getPartnerName());
		partner.setPartnerDetails(partnerDTO.getPartnerDetails());

		Partner updatedPartner = partnerRepository.save(partner);
		return partnerMapper.toDTO(updatedPartner);
	}

	@Override
	public void deletePartner(Integer id) throws PartnerNotFoundException {
		if (!partnerRepository.existsById(id)) {
			throw new PartnerNotFoundException(partnerNotFoundMsg + id);
		}
		partnerRepository.deleteById(id);
	}

 
	
	@Override
	public List<PartnerDTO> findPartnersByName(String name) {
	    List<Partner> partners = partnerRepository.findByPartnerNameContainingIgnoreCase(name);
	    if (partners.isEmpty()) {
	        System.out.println("No partners found for name: " + name);
	        return null;
	    }
	    return partners.stream().map(partnerMapper::toDTO).toList();
	            
	}


	@Override
	public PartnerDTO getPartnerByCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}

		// Check if user is a PARTNER_REP
		if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTNER_REP"))) {
			return null;
		}

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Integer userId = userDetails.getId();

		Optional<User> user = userRepository.findById(userId);
		if (user.isPresent()) {
			return partnerMapper.toDTO(user.get().getPartner());
		}

		return null;
	}

	// ---------------------
	@Override
	@Transactional
	public boolean assignPartnerToEvent(Integer partnerId, Integer eventId, Integer roleId) {
		Optional<Partner> partnerOpt = partnerRepository.findById(partnerId);
		Optional<Event> eventOpt = eventRepository.findById(eventId);
		Optional<PartnerRole> roleOpt = partnerRoleRepository.findById(roleId);

		if (partnerOpt.isPresent() && eventOpt.isPresent() && roleOpt.isPresent()) {
			// Check if assignment already exists
			List<IsPartner> existingAssignments = isPartnerRepository.findAllByEvent_id(eventId);

			boolean assignmentExists = existingAssignments.stream()
					.anyMatch(assignment -> assignment.getPartner().getId().equals(partnerId)
							&& assignment.getPartnerRole().getId().equals(roleId));

			if (assignmentExists) {
				return false; // Assignment already exists
			}

			// Create new assignment
			IsPartner newAssignment = new IsPartner();
			newAssignment.setPartner(partnerOpt.get());
			newAssignment.setEvent(eventOpt.get());
			newAssignment.setPartnerRole(roleOpt.get());

			isPartnerRepository.save(newAssignment);
			return true;
		}

		return false;
	}

	@Override
	public List<PartnerDTO> findPartnersByEventAndRole(Integer eventId, Integer roleId) {
		List<Partner> partners = partnerRepository.findPartnersByEventAndRole(eventId, roleId);
		return partners.stream().map(partnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<PartnerDTO> findPartnersWithMinimumEvents(long minEvents) {
		List<Partner> partners = partnerRepository.findPartnersWithMinimumEvents(minEvents);
		return partners.stream().map(partnerMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Map<String, Long> getPartnerEventCountStats() {
		List<Partner> allPartners = partnerRepository.findAll();
		Map<String, Long> result = new HashMap<>();

		for (Partner partner : allPartners) {
			result.put(partner.getPartnerName(), (long) partner.getPartnerEvents().size());
		}

		return result;
	}

	@Override
	public long countPartners() {
		 return partnerRepository.count();
	}
}
