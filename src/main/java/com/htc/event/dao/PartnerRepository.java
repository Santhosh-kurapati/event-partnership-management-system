package com.htc.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.htc.event.entity.Partner;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    
    // Find partners by role for a specific event
    @Query("SELECT p FROM Partner p JOIN p.partnerEvents pe WHERE pe.event.id = :eventId AND pe.partnerRole.id = :roleId")
    List<Partner> findPartnersByEventAndRole(
            @Param("eventId") Integer eventId,
            @Param("roleId") Integer roleId);
    
    // Find partners that participated in more than a certain number of events
    @Query("SELECT p FROM Partner p JOIN p.partnerEvents pe GROUP BY p.id HAVING COUNT(pe.event) >= :minEvents")
    List<Partner> findPartnersWithMinimumEvents(@Param("minEvents") long minEvents);

    List<Partner> findByPartnerNameContainingIgnoreCase(String name);

}
