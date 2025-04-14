package com.htc.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.htc.event.entity.PartnerRole;

import java.util.List;

@Repository
public interface PartnerRoleRepository extends JpaRepository<PartnerRole, Integer> {
    
    // Find roles used in a specific event
    @Query("SELECT pr FROM PartnerRole pr JOIN pr.roleAssignments ra WHERE ra.event.id = :eventId")
    List<PartnerRole> findRolesByEvent(@Param("eventId") Integer eventId);
    
    // Find most common partner roles
    @Query("SELECT pr, COUNT(ra) as roleCount FROM PartnerRole pr JOIN pr.roleAssignments ra GROUP BY pr.id ORDER BY roleCount DESC")
    List<Object[]> findMostCommonRoles();
}
