package com.htc.event.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.event.dto.EventTypeDTO;
import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.entity.EventType;
import com.htc.event.entity.IsPartner;

@Component
public class IsPartnerMapper {
    
    private ModelMapper modelMapper;

    @Autowired
    public IsPartnerMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public IsPartnerDTO toDTO(IsPartner isPartner) {
    	if (isPartner == null) {
            return null;
        }
        return modelMapper.map(isPartner, IsPartnerDTO.class);
    }

    public IsPartner toEntity(IsPartnerDTO isPartnerDTO) {
    	if (isPartnerDTO == null) {
            return null;
        }
        return modelMapper.map(isPartnerDTO, IsPartner.class);
    }
}
