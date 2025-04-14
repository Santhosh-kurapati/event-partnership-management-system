package com.htc.event.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.event.dto.EventTypeDTO;
import com.htc.event.entity.EventType;

@Component
public class EventTypeMapper {
        
    private ModelMapper modelMapper;
    
    @Autowired
    public EventTypeMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public EventTypeDTO toDTO(EventType eventType) {
    	if (eventType == null) {
            return null;
        }
        return modelMapper.map(eventType, EventTypeDTO.class);
    }

    public EventType toEntity(EventTypeDTO eventTypeDTO) {
    	if (eventTypeDTO == null) {
            return null;
        }
        return modelMapper.map(eventTypeDTO, EventType.class);
    }
}
