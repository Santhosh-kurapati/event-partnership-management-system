package com.htc.event.service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.htc.event.dto.PartnerRoleDTO;
import com.htc.event.entity.PartnerRole;
import com.htc.event.exception.PartnerRoleNotFoundException;

public interface PartnerRoleService {
    List<PartnerRoleDTO> getAllPartnerRoles();
    Optional<PartnerRoleDTO> getPartnerRoleById(Integer id) throws PartnerRoleNotFoundException;
    void deletePartnerRole(Integer id) throws PartnerRoleNotFoundException;
    List<PartnerRoleDTO> findRolesByEvent(Integer eventId);
    Map<String, Long> getMostCommonRoles();
	PartnerRoleDTO savePartnerRole(PartnerRoleDTO partnerRoleDTO);
	PartnerRoleDTO updatePartnerRole(PartnerRoleDTO partnerRoleDTO) throws PartnerRoleNotFoundException;
	//List<PartnerRole> findPartnerRolesByName(String name);
	long countPartnerRoles();
}
