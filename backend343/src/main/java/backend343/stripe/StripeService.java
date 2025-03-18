package backend343.stripe;

import backend343.logger.LoggerSingleton;
import backend343.models.Session;
import backend343.models.User;
import backend343.repository.UserRepository;
import backend343.service.SessionService;
import com.stripe.exception.StripeException;


import com.stripe.model.PromotionCode;
import com.stripe.param.PromotionCodeListParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.util.List;
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
        BigDecimal price = session.getSchedule().getEvent().getPrice().multiply(BigDecimal.valueOf(100));
        String promoCode = productRequest.getPromoCode();
        BigDecimal percentOff = getPercentOffForPromoCode(promoCode, session.getStripeProductId());

        logger.logInfo("Checkout promo code: " + promoCode);
        if (percentOff != null) {
            BigDecimal discountFactor = (percentOff).divide(BigDecimal.valueOf(100));
            price = price.multiply(BigDecimal.valueOf(1).subtract(discountFactor));
        }


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

    public BigDecimal getPercentOffForPromoCode(String promoCode, String stripeProductId) {
        try {
            // Retrieve the promotion code by filtering using the user-facing promo code
            PromotionCodeListParams params = PromotionCodeListParams.builder()
                    .setCode(promoCode) // Search by the actual code customers use
                    .setLimit(1L) // We only need one match
                    .build();

            List<PromotionCode> promoCodes = PromotionCode.list(params).getData();

            if (promoCodes.isEmpty()) {
                logger.logError("Promo code not found: " + promoCode);
                return null;
            }

            // Get the first (and should be the only) matching promo code
            PromotionCode promo = promoCodes.get(0);
            logger.logInfo("Found promo code: " + promo.getId() + " with code: " + promo.getCode());

            // Retrieve metadata for product validation
            String storedProductId = promo.getMetadata().get("stripe_product_id");

            if (storedProductId == null || storedProductId.isEmpty()) {
                // No specific product restriction, so it's valid
                return promo.getCoupon().getPercentOff();
            } else if (storedProductId.equals(stripeProductId)) {
                // Product restriction matches, so it's valid
                return promo.getCoupon().getPercentOff();
            } else {
                // Product restriction doesn't match, so it's invalid
                return null;
            }
        } catch (StripeException e) {
            logger.logError("Error retrieving promo code: " + e.getMessage());
            return null;
        }
    }
}
