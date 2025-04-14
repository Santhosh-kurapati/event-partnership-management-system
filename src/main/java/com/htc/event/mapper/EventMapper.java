package com.htc.event.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.htc.event.dto.EventDTO;
import com.htc.event.dto.IsPartnerDTO;
import com.htc.event.entity.Event;
import com.htc.event.entity.EventType;
import com.htc.event.entity.IsPartner;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private  ModelMapper modelMapper;

    @Autowired
    public EventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convert Entity to DTO
    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
        
        

        return eventDTO;
    }

    // Convert DTO to Entity
    public Event toEntity(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        Event event = modelMapper.map(eventDTO, Event.class);

        

        return event;
    }
}
