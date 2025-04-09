package backend343.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend343.models.EventInvestment;

@Repository
public interface EventInvestmentRepository extends JpaRepository<EventInvestment, Long>{
    
    List<EventInvestment> findByStakeholderId(Long stakeholderId);
    
    List<EventInvestment> findByEventId(Long eventId);
}
