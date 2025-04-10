package backend343.stripe;

import backend343.logger.LoggerSingleton;
import backend343.models.Event;
import backend343.models.Schedule;
import backend343.models.Ticket;
import backend343.models.User;
import backend343.service.*;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class StripeWebhookController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    SessionService sessionService;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private EmailService emailService;

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @PostMapping("/stripe-webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        String endpointSecret = "whsec_fd0626eef51aedead194fef3c6c78388cdb4aa7019242f1b82118b025a32096a";

        try {
            com.stripe.model.Event event = Webhook.constructEvent(payload, signature, endpointSecret);

            if (event.getType().equals("checkout.session.completed")) {
                logger.logInfo("Checkout session completed");
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid session object"));
                handleCheckoutSessionCompleted(session);
            }

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (SignatureVerificationException e) {
            logger.logError("Invalid signature: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            logger.logError("Error processing webhook: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private void handleCheckoutSessionCompleted(Session session) {
        logger.logInfo("Handling checkout session completion");
        String strSessionId = session.getMetadata().get("session_id");
        String strUserId = session.getMetadata().get("user_id");

        long sessionId = Long.parseLong(strSessionId);
        long userId = Long.parseLong(strUserId);

        backend343.models.Session sessionEvent = sessionService.getSessionById(sessionId);
        User user = userDetailsService.getUserById(userId);
        String stripePaymentId = session.getPaymentIntent();
        long amountPaid = session.getAmountTotal();
        BigDecimal amountPaidDecimal = BigDecimal.valueOf(amountPaid).divide(BigDecimal.valueOf(100)); // amount in
                                                                                                       // dollars
        createTicket(sessionEvent, user, amountPaidDecimal, stripePaymentId);
    }

    private void createTicket(backend343.models.Session sessionEvent, User user, BigDecimal amountPaid,
            String stripePaymentId) {
        logger.logInfo("Creating ticket for user " + user.getId());
        Ticket ticket = ticketService.createTicket(sessionEvent, user, stripePaymentId, amountPaid);
        sendConfirmationOfPurchaseEmail(user, ticket, amountPaid);
    }

    private void sendConfirmationOfPurchaseEmail(User user, Ticket ticket, BigDecimal amountPaid) {
        logger.logInfo("Sending confirmation email to user " + user.getEmail());
        backend343.models.Session session = ticket.getSession();
        Schedule schedule = session.getSchedule();
        Event event = schedule.getEvent();

        String subject = "Confirmation of Purchase: " + session.getTitle();
        String htmlMessage = "<h1>Confirmation of Purchase</h1>" +
                "<p>Thank you for purchasing a ticket to " + session.getTitle() + ".</p>" +
                "<p>Ticket Code: <strong>" + ticket.getTicketCode() + "</strong></p>" +
                "<p>You have paid a total of: <strong>$" + amountPaid + "</strong></p>" +
                "<p>Event Details:</p>" +
                "<p><strong>Event Name:</strong> " + event.getName() + "</p>" +
                "<p>Schedule Details:</p>" +
                "<p><strong>Schedule Date:</strong> " + schedule.getDate() + "</p>" +
                "<p><strong>Session Time:</strong> " + session.getStartTime() + " - " + session.getEndTime() + "</p>" +
                "<p><strong>Session Location:</strong> " + session.getLocation() + "</p>" +
                "<p>Please note that you will be required to provide this ticket code when checking in at the event.</p>";

        emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        logger.logInfo("Confirmation email sent successfully to user " + user.getEmail());
    }
}
