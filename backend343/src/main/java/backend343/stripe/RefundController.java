package backend343.stripe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refund/v1")
@RequiredArgsConstructor
public class RefundController {
    private final StripeService stripeService;
    @PostMapping("/refund")
    public ResponseEntity<StripeResponse> refundPayment(@RequestBody RefundRequest refundRequest) {
        return ResponseEntity.ok(stripeService.refundPayment(refundRequest.getSessionId(), refundRequest.getUserId()));
    }
}
