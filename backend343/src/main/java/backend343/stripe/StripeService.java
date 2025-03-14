package backend343.stripe;

import backend343.logger.LoggerSingleton;
import backend343.models.Session;
import backend343.models.User;
import backend343.repository.UserRepository;
import backend343.service.SessionService;
import com.stripe.exception.StripeException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.util.Map;


@Service
public class StripeService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserRepository userRepository;

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();


    public StripeResponse checkoutEvent(ProductRequest productRequest) {
        logger.logInfo("Initiating checkout for product request: " + productRequest);

        Session session = sessionService.getSessionById(productRequest.getSessionId());
        User user = userRepository.findByEmail(productRequest.getUserEmail()).orElseThrow();

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                .builder()
                .setName(session.getTitle()).build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("CAD")
                .setUnitAmount(session.getSchedule().getEvent().getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(productData).build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .setCustomerEmail(productRequest.getUserEmail())
                .setAllowPromotionCodes(true)
                .putAllMetadata(Map.of(
                        "session_id", String.valueOf(productRequest.getSessionId()),
                        "user_id", String.valueOf(user.getId())
                ))
                .build();

        com.stripe.model.checkout.Session checkoutSession = null;
        try {
            logger.logInfo("Creating Stripe checkout session...");
            checkoutSession = com.stripe.model.checkout.Session.create(params);
            logger.logInfo("Stripe checkout session created successfully");
        } catch (StripeException ex) {
            logger.logError("Error creating Stripe checkout session: " + ex.getMessage());
            ex.printStackTrace();
        }

        if (checkoutSession == null) {
            logger.logError("Checkout session creation failed");
            return StripeResponse.builder()
                    .status("FAILURE")
                    .message("Payment session creation failed")
                    .build();
        }

        logger.logInfo("Returning successful checkout response");
        return StripeResponse.builder()
                .status("SUCCESS")
                .message("Payment session created successfully")
                .sessionId(checkoutSession.getId())
                .sessionUrl(checkoutSession.getUrl())
                .build();
    }
}
