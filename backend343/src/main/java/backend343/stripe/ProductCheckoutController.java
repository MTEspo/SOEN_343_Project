package backend343.stripe;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/v1")
public class ProductCheckoutController {

    private StripeService stripeService;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }
    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(stripeService.checkoutEvent(productRequest));
    }
}
