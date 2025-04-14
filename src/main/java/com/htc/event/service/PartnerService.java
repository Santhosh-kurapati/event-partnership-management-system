package com.htc.event.service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.htc.event.dto.PartnerDTO;
import com.htc.event.entity.Partner;
import com.htc.event.exception.PartnerNotFoundException;

public interface PartnerService {
    List<PartnerDTO> getAllPartners();
    Optional<PartnerDTO> getPartnerById(Integer id) throws PartnerNotFoundException;
    void deletePartner(Integer id) throws PartnerNotFoundException;
    List<PartnerDTO> findPartnersByEventAndRole(Integer eventId, Integer roleId);
    List<PartnerDTO> findPartnersWithMinimumEvents(long minEvents);
    Map<String, Long> getPartnerEventCountStats();
    boolean assignPartnerToEvent(Integer partnerId, Integer eventId, Integer roleId);
	PartnerDTO savePartner(PartnerDTO partnerDTO);
	PartnerDTO updatePartner(PartnerDTO partnerDTO) throws PartnerNotFoundException;
	List<PartnerDTO> findPartnersByName(String name);
	PartnerDTO getPartnerByCurrentUser();
	long countPartners();
}
