package backend343.stripe;

import lombok.Data;

@Data
public class RefundRequest {
    private Long sessionId;
    private Long userId;
}