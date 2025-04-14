package com.htc.event.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.dto.PartnerDTO;
import com.htc.event.entity.IsPartner;
import com.htc.event.entity.Partner;

@Component
public class PartnerMapper {
    
    
    private ModelMapper modelMapper;

    @Autowired
    public PartnerMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public PartnerDTO toDTO(Partner partner) {
    	if (partner == null) {
            return null;
        }
        return modelMapper.map(partner, PartnerDTO.class);
    }

    public Partner toEntity(PartnerDTO partnerDTO) {
    	if (partnerDTO == null) {
            return null;
        }
        return modelMapper.map(partnerDTO, Partner.class);
    }
}
