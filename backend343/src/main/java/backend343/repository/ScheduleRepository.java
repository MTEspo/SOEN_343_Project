package backend343.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend343.models.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
    
}
