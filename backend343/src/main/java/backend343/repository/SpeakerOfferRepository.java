package backend343.repository;

import backend343.models.SpeakerOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeakerOfferRepository extends JpaRepository<SpeakerOffer, Long> {
    List<SpeakerOffer> findBySpeakerId(Long speakerId);
}
