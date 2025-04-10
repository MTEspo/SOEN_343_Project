package backend343.dto;

import backend343.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSpeakerOfferRequest {
    private Long offerId;
    private OfferStatus status;
}
