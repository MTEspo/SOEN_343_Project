package backend343.repository;

import backend343.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByEventId(Long eventId);
    Resource findByIdAndEventId(Long id, Long eventId);
    void deleteByIdAndEventId(Long id, Long eventId);
}
