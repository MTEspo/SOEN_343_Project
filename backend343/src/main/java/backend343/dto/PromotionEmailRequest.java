package backend343.dto;

import lombok.Data;

@Data
public class PromotionEmailRequest {
    private String subject;
    private String text;
    private Long sessionId;
    private boolean hasDiscount;
    private int percentageOff;
}
