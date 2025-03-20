package backend343.service;

import backend343.models.Organizer;
import backend343.models.Session;
import backend343.models.Speaker;
import backend343.models.SpeakerOffer;
import backend343.repository.SpeakerOfferRepository;
import backend343.repository.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import backend343.enums.OfferStatus;

import java.util.List;

@Service
public class SpeakerOfferService {

    @Autowired
    private SpeakerOfferRepository speakerOfferRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private OrganizerService organizerService;

    @Transactional
    public void createSpeakerOffer(Long sessionId, Long speakerId, Long organizerId) {
        Session session = sessionService.getSessionById(sessionId);
        Speaker speaker = speakerRepository.findById(speakerId).orElseThrow();
        Organizer organizer = organizerService.findOrganizerById(organizerId);

        SpeakerOffer speakerOffer = SpeakerOffer.builder()
                .session(session)
                .speaker(speaker)
                .organizer(organizer)
                .status(OfferStatus.PENDING)
                .build();

        speakerOfferRepository.save(speakerOffer);

        emailService.sendEmail(speaker.getEmail(), "Speaker Offer", "You have been offered to speak at session " + session.getTitle());
    }

    @Transactional
    public void updateSpeakerOffer(Long offerId, OfferStatus status) {
        SpeakerOffer speakerOffer = speakerOfferRepository.findById(offerId).orElseThrow();

        if (status == OfferStatus.ACCEPTED) {
            Session session = speakerOffer.getSession();
            session.setSpeaker(speakerOffer.getSpeaker());
            sessionService.saveSession(session);
            Speaker speaker = speakerOffer.getSpeaker();
            speaker.getSessions().add(session);
            speakerRepository.save(speaker);

            Organizer organizer = speakerOffer.getOrganizer();
            emailService.sendEmail(organizer.getEmail(), "Speaker Accepted Offer", "Speaker " + speakerOffer.getSpeaker().getUsername() + " has accepted the offer to speak at session " + session.getTitle());
        } else if (status == OfferStatus.DECLINED) {
            Organizer organizer = speakerOffer.getOrganizer();
            emailService.sendEmail(organizer.getEmail(), "Speaker Declined Offer", "Speaker " + speakerOffer.getSpeaker().getUsername() + " has declined the offer to speak at session " + speakerOffer.getSession().getTitle());
        }

        speakerOfferRepository.delete(speakerOffer);
    }

    public List<SpeakerOffer> findBySpeakerId(Long speakerId) {
        return speakerOfferRepository.findBySpeakerId(speakerId);
    }
}
