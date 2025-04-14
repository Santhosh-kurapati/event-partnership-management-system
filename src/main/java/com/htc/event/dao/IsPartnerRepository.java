package com.htc.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.htc.event.entity.IsPartner;

import java.util.List;

@Repository
public interface IsPartnerRepository extends JpaRepository<IsPartner, Integer> {
    
    // Find partner assignments for a specific event
    List<IsPartner> findAllByEvent_id(Integer eventId);
    
    // Find partner assignments for a specific partner
    List<IsPartner> findAllByPartner_id(Integer partnerId);
    
    // Find partner assignments for a specific role
    List<IsPartner> findAllByPartnerRole_id(Integer roleId);

    
    // Find events where a partner has a specific role
    @Query("SELECT ip FROM IsPartner ip WHERE ip.partner.id = :partnerId AND ip.partnerRole.id = :roleId")
    List<IsPartner> findEventsByPartnerAndRole(
            @Param("partnerId") Integer partnerId, 
            @Param("roleId") Integer roleId);
}
