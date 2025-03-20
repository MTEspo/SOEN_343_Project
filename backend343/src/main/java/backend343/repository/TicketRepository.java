package backend343.repository;

import backend343.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Ticket t WHERE t.user.id = :userId AND t.session.id = :sessionId AND t.status = backend343.enums.TicketStatus.ACTIVE")
    boolean hasEventAccessAndActiveStatus(@Param("userId") Long userId, @Param("sessionId") Long sessionId);

    Ticket findByTicketCode(String ticketCode);

    Optional<Ticket> findBySessionIdAndUserId(Long sessionId, Long userId);

    List<Ticket> findAllByUserId(Long userId);

}
