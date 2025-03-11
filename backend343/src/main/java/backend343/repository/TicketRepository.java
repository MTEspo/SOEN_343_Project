package backend343.repository;

import backend343.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT CASE WHEN COUNT(ue) > 0 THEN true ELSE false END FROM Ticket ue WHERE ue.user.id = :userId AND ue.event.id = :eventId")
    boolean hasEventAccess(@Param("userId") Long userId, @Param("eventId") Long eventId);

    Ticket findByTicketCode(String ticketCode);
}
