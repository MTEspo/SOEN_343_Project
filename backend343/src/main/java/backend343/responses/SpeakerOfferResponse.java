package backend343.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpeakerOfferResponse {
    private boolean success;
    private String message;
}
