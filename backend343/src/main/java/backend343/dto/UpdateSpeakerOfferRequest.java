package backend343.dto;

import backend343.enums.OfferStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSpeakerOfferRequest {
    private Long offerId;
    private OfferStatus status;
}
