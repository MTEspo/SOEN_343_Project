package backend343.repository;

import backend343.models.Speaker;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    Speaker findByEmail(String email);

    @Query("SELECT s FROM Speaker s JOIN s.sessions sess WHERE sess.chatroom.id = :chatroomId")
    List<Speaker> findSpeakersByChatroomId(@Param("chatroomId") Long chatroomId);
}
