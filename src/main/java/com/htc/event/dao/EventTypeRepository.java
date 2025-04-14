package com.htc.event.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.htc.event.entity.Event;
import com.htc.event.entity.Event.EventStatus;
import com.htc.event.entity.EventType;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    
    // Find event types with events in a specific status
    @Query("SELECT DISTINCT et FROM EventType et JOIN et.events e WHERE e.eventStatus = :status")
    List<EventType> findEventTypesWithEventStatus(@Param("status") EventStatus status);
    
    // Find event types with most events
    @Query("SELECT et, COUNT(e) as eventCount FROM EventType et JOIN et.events e GROUP BY et.id ORDER BY eventCount DESC")
    List<Object[]> findEventTypesByPopularity();
}
