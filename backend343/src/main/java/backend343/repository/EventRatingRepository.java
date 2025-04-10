package backend343.repository;

import backend343.models.EventRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRatingRepository extends JpaRepository<EventRating, Long> {
    List<EventRating> findByEventId(Long eventId);
    boolean existsByEventIdAndRaterId(Long eventId, Long userId);


}
