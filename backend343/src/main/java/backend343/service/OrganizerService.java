package backend343.service;

import backend343.dto.PromotionEmailRequest;
import backend343.models.Session;
import backend343.models.User;
import backend343.repository.OrganizerRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend343.factory.*;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.PromotionCode;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.PromotionCodeCreateParams;
import java.math.BigDecimal;


import java.util.List;

import static backend343.authentication.AuthenticationService.generateCouponCode;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepository organizerRepostiory;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SessionService sessionService;

    public void sendEmail(PromotionEmailRequest promotionEmailRequest) throws MessagingException, StripeException {
        Long sessionId = promotionEmailRequest.getSessionId();

        List<User> users = userDetailsService.getAllUsers();
        for (User user : users) {
            String emailText = promotionEmailRequest.getText();

            if (promotionEmailRequest.isHasDiscount()) {
                String discountCode = createCouponForSession(sessionId, promotionEmailRequest.getPercentageOff());
                emailText += " Use code " + discountCode + " at checkout to receive " + promotionEmailRequest.getPercentageOff() + "% off.";
            }

            emailService.sendEmail(user.getEmail(), promotionEmailRequest.getSubject(), emailText);
        }
    }

    public String createCouponForSession(Long sessionId, int percentOff) throws StripeException {
        Session session = sessionService.getSessionById(sessionId);
        String stripeProductId = session.getStripeProductId(); // This is the Stripe Product ID

        String couponCode = generateCouponCode();
        CouponCreateParams couponParams = CouponCreateParams.builder()
                .setId(couponCode)
                .setPercentOff(BigDecimal.valueOf(percentOff))
                .setMaxRedemptions(1L)
                .putMetadata("stripe_product_id", stripeProductId)
                .build();
        Coupon coupon = Coupon.create(couponParams);

        PromotionCodeCreateParams promotionCodeParams = PromotionCodeCreateParams.builder()
                .setCoupon(coupon.getId())
                .setCode(couponCode)
                .putMetadata("stripe_product_id", stripeProductId)
                .setMaxRedemptions(1L)
                .build();
        PromotionCode promoCode = PromotionCode.create(promotionCodeParams);

        return promoCode.getCode();
    }

}



