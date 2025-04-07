package backend343.dto;

import lombok.Data;

@Data
public class PromotionEmailRequest {
    private String subject;
    private String text;
}
