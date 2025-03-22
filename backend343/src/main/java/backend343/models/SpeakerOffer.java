package backend343.models;

import backend343.enums.OfferStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakerOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @JsonIgnore
    private Session session;

    @ManyToOne
    @JoinColumn(name = "speaker_id")
    @JsonIgnore
    private Speaker speaker;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    @JsonIgnore
    private Organizer organizer;
}
