package backend343.repository;

import backend343.models.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END FROM UserEvent ue WHERE ue.user.id = :userId AND ue.event.id = :eventId")
    boolean hasEventAccess(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
