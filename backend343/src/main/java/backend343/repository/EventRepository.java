package backend343.repository;

import backend343.enums.Tag;
import backend343.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE EXISTS (SELECT t FROM e.tags t WHERE t IN :tags) ORDER BY SIZE(e.tags) DESC")
    List<Event> findEventsByTags(@Param("tags") List<Tag> tags);
}
