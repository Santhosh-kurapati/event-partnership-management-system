package com.htc.event.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.dao.PartnerRoleRepository;
import com.htc.event.entity.PartnerRole;
import com.htc.event.exception.PartnerRoleNotFoundException;
import com.htc.event.service.PartnerRoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.dto.PartnerRoleDTO;
import com.htc.event.mapper.PartnerRoleMapper;
import com.htc.event.service.PartnerRoleService;

@Service
@Transactional
public class PartnerRoleServiceImpl implements PartnerRoleService {

	private PartnerRoleRepository partnerRoleRepository;

	private PartnerRoleMapper partnerRoleMapper;

	@Autowired
	public PartnerRoleServiceImpl(PartnerRoleRepository partnerRoleRepository, PartnerRoleMapper partnerRoleMapper) {
		super();
		this.partnerRoleRepository = partnerRoleRepository;
		this.partnerRoleMapper = partnerRoleMapper;
	}

	@Override
	public PartnerRoleDTO savePartnerRole(PartnerRoleDTO partnerRoleDTO) {
		PartnerRole partnerRole = new PartnerRole();
		partnerRole.setRoleName(partnerRoleDTO.getRoleName());

		PartnerRole savedPartnerRole = partnerRoleRepository.save(partnerRole);
		return partnerRoleMapper.toDTO(savedPartnerRole);
	}

	@Override
	public Optional<PartnerRoleDTO> getPartnerRoleById(Integer id) throws PartnerRoleNotFoundException {
		return Optional.of(partnerRoleMapper.toDTO(partnerRoleRepository.findById(id)
				.orElseThrow(() -> new PartnerRoleNotFoundException("PartnerRole not found for id: " + id))));
	}

	@Override
	public List<PartnerRoleDTO> getAllPartnerRoles() {
		return partnerRoleRepository.findAll().stream().map(partnerRoleMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public PartnerRoleDTO updatePartnerRole(PartnerRoleDTO partnerRoleDTO) throws PartnerRoleNotFoundException {
		PartnerRole partnerRole = partnerRoleRepository.findById(partnerRoleDTO.getId()).orElseThrow(
				() -> new PartnerRoleNotFoundException("PartnerRole not found for id: " + partnerRoleDTO.getId()));

		partnerRole.setRoleName(partnerRoleDTO.getRoleName());

		PartnerRole updatedPartnerRole = partnerRoleRepository.save(partnerRole);
		return partnerRoleMapper.toDTO(updatedPartnerRole);
	}

	@Override
	public void deletePartnerRole(Integer id) throws PartnerRoleNotFoundException {
		if (!partnerRoleRepository.existsById(id)) {
			throw new PartnerRoleNotFoundException("PartnerRole not found for id: " + id);
		}
		partnerRoleRepository.deleteById(id);
	}

	// -------------------------
	@Override
	public List<PartnerRoleDTO> findRolesByEvent(Integer eventId) {
		List<PartnerRole> roles = partnerRoleRepository.findRolesByEvent(eventId);
		return roles.stream().map(partnerRoleMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public Map<String, Long> getMostCommonRoles() {
		List<Object[]> results = partnerRoleRepository.findMostCommonRoles();
		Map<String, Long> roleCountMap = new HashMap<>();

		for (Object[] result : results) {
			PartnerRole role = (PartnerRole) result[0];
			Long count = (Long) result[1];
			roleCountMap.put(role.getRoleName(), count);
		}

		return roleCountMap;
	}

	@Override
	public long countPartnerRoles() {
		return partnerRoleRepository.count();
	}
}
