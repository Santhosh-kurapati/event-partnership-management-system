package com.htc.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.htc.event.entity.Event;
import com.htc.event.entity.Event.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
    @Query("SELECT e FROM Event e WHERE e.eventStatus = :status AND DATE(e.startTime) BETWEEN :startDate AND :endDate")
    List<Event> findEventsByStatusAndDateRange(
            @Param("status") EventStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Event e JOIN e.eventPartners ep WHERE ep.partnerRole.roleName = :roleName")
    List<Event> findEventsByPartnerRole(@Param("roleName") String roleName);
 
    //if multiple events having same name
    List<Event> findByEventName(String eventName);

}

