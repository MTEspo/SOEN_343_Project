package backend343.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InvestedEventDto {
    private Long eventId;
    private String eventName;
    private String eventDescription;
    private BigDecimal amountInvested;
}