package backend343.service;

import backend343.models.Session;
import backend343.models.Speaker;
import backend343.models.SpeakerOffer;
import backend343.repository.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final SpeakerOfferService speakerOfferService;

    public Speaker findById(Long id) {
        return speakerRepository.findById(id).orElseThrow();
    }

    public Speaker findByEmail(String email) {
        return speakerRepository.findByEmail(email);
    }

    public String getUsernameById(Long speakerId){
        return findById(speakerId).getUsername();
    }

    public Double getAverageRating(Long speakerId){
        return findById(speakerId).getAverageRating();
    }

    public void updateSpeaker(Speaker speaker){
        speakerRepository.save(speaker);
    }

    public List<Session> getSessionsBySpeakerId(Long speakerId) {
        Speaker speaker = findById(speakerId);
        return speaker.getSessions();
    }

    public void saveSpeaker(Speaker speaker) {
        speakerRepository.save(speaker);
    }

    public List<SpeakerOffer> getAllOffers(Long speakerId) {
        return speakerOfferService.findBySpeakerId(speakerId);
    }
}
