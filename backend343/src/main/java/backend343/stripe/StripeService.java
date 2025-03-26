package backend343.stripe;

import backend343.enums.TicketStatus;
import backend343.logger.LoggerSingleton;
import backend343.models.*;
import backend343.repository.UserRepository;
import backend343.service.EmailService;
import backend343.service.SessionService;
import backend343.service.TicketService;
import backend343.service.UserDetailsServiceImpl;
import com.stripe.exception.StripeException;

import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import com.stripe.param.RefundCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class StripeService {

        @Autowired
        private SessionService sessionService;
        @Autowired
        private UserDetailsServiceImpl userDetailsService;

        @Autowired
        private TicketService ticketService;

        @Autowired
        private EmailService emailService;

        private static final LoggerSingleton logger = LoggerSingleton.getInstance();

        public StripeResponse checkoutEvent(ProductRequest productRequest) {
                logger.logInfo("Initiating checkout for product request: " + productRequest);

                Session session = sessionService.getSessionById(productRequest.getSessionId());
                User user = userDetailsService.findByEmail(productRequest.getUserEmail());

                SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
                                .builder()
                                .setName(session.getTitle()).build();

                SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("CAD")
                                .setUnitAmount(session.getSchedule().getEvent().getPrice()
                                                .multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(productData).build();

                SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(priceData)
                                .build();

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("http://localhost:3000/success")
                                .setCancelUrl("http://localhost:3000/cancel")
                                .addLineItem(lineItem)
                                .setCustomerEmail(productRequest.getUserEmail())
                                .setAllowPromotionCodes(true)
                                .putAllMetadata(Map.of(
                                                "session_id", String.valueOf(productRequest.getSessionId()),
                                                "user_id", String.valueOf(user.getId())))
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

        public StripeResponse refundPayment(Long sessionId, Long userId) {
                logger.logInfo("Initiating refund for session ID " + sessionId + " and user ID " + userId);

                Ticket ticket = ticketService.getTicketBySessionIdAndUserId(sessionId, userId);
                if (ticket == null) {
                        logger.logError("Ticket not found for session ID " + sessionId + " and user ID " + userId);
                        return StripeResponse.builder()
                                        .status("FAILURE")
                                        .message("Ticket not found")
                                        .build();
                }

                if (ticket.getStatus() == TicketStatus.REFUNDED) {
                        logger.logError("Ticket has already been refunded");
                        return StripeResponse.builder()
                                        .status("FAILURE")
                                        .message("Ticket has already been refunded")
                                        .build();
                }

                Session session = sessionService.getSessionById(sessionId);
                Schedule schedule = session.getSchedule();
                Event event = schedule.getEvent();

                if (schedule.getDate().isBefore(LocalDate.now()) ||
                                (schedule.getDate().isEqual(LocalDate.now())
                                                && session.getStartTime().isBefore(LocalTime.now()))) {
                        logger.logError("Refund not allowed for session that has already started or passed");
                        return StripeResponse.builder()
                                        .status("FAILURE")
                                        .message("Refund not allowed for session that has already started or passed")
                                        .build();
                }

                String paymentIntentId = ticket.getStripePaymentId();
                if (paymentIntentId == null || paymentIntentId.isEmpty()) {
                        logger.logError("No payment intent found for the ticket");
                        return StripeResponse.builder()
                                        .status("FAILURE")
                                        .message("Payment intent not found")
                                        .build();
                }

                try {
                        String idempotencyKey = "refund_" + paymentIntentId + "_" + userId;
                        RefundCreateParams refundParams = RefundCreateParams.builder()
                                        .setPaymentIntent(paymentIntentId)
                                        .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                                        .build();

                        RequestOptions requestOptions = RequestOptions.builder()
                                        .setIdempotencyKey(idempotencyKey)
                                        .build();

                        Refund refund = Refund.create(refundParams, requestOptions);
                        logger.logInfo("Refund successful for payment intent " + paymentIntentId);

                        String userEmail = userDetailsService.getUserById(userId).getEmail();
                        String emailSubject = "Refund Confirmation";
                        String emailText = "Your refund of $" + refund.getAmount() / 100 + " for the event '"
                                        + event.getName() + "' on " + schedule.getDate() + " at "
                                        + session.getStartTime() + " has been processed successfully.";
                        emailService.sendEmail(userEmail, emailSubject, emailText);
                        ticketService.refundTicket(ticket);
                        return StripeResponse.builder()
                                        .status("SUCCESS")
                                        .message("Refund successful")
                                        .build();

                } catch (StripeException e) {
                        logger.logError("Error refunding payment: " + e.getMessage());
                        return StripeResponse.builder()
                                        .status("FAILURE")
                                        .message("Error refunding payment: " + e.getMessage())
                                        .build();
                }
        }

}
