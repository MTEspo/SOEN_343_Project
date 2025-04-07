package backend343.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSpeakerOfferRequest {
    private Long sessionId;
    private Long speakerId;
    private Long organizerId;
}
