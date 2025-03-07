package backend343.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class StripeConfig {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String publicKey;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void configureStripe() {
        Stripe.apiKey = secretKey;
    }
}
