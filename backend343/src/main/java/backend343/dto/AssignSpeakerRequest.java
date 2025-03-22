package backend343.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignSpeakerRequest {
    private Long speakerId;
    private Long sessionId;
}
