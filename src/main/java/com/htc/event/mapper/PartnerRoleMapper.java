package com.htc.event.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.dto.PartnerRoleDTO;
import com.htc.event.entity.IsPartner;
import com.htc.event.entity.PartnerRole;

@Component
public class PartnerRoleMapper {
    	
    private ModelMapper modelMapper;
    @Autowired
    public PartnerRoleMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public PartnerRoleDTO toDTO(PartnerRole partnerRole) {
    	if (partnerRole == null) {
            return null;
        }
        return modelMapper.map(partnerRole, PartnerRoleDTO.class);
    }

    public PartnerRole toEntity(PartnerRoleDTO partnerRoleDTO) {
    	if (partnerRoleDTO == null) {
            return null;
        }
        return modelMapper.map(partnerRoleDTO, PartnerRole.class);
    }
}

