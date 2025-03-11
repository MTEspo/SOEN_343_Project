package backend343.stripe;
import backend343.models.Event;
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

    @PostMapping("/stripe-webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signature) {
        String endpointSecret = "";

        try {
            com.stripe.model.Event event = Webhook.constructEvent(payload, signature, endpointSecret);

            if (event.getType().equals("checkout.session.completed")) {
                System.out.println("here");
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid session object"));
                handleCheckoutSessionCompleted(session);
            }

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }

    private void handleCheckoutSessionCompleted(Session session) {
        String strSessionId = session.getMetadata().get("session_id");
        String strUserId = session.getMetadata().get("user_id");

        long sessionId = Long.parseLong(strSessionId);
        long userId = Long.parseLong(strUserId);

        backend343.models.Session sessionEvent  = sessionService.getSessionById(sessionId);
        User user = userDetailsService.getUserById(userId);
        createTicket(sessionEvent, user);
    }

    private void createTicket(backend343.models.Session sessionEvent, User user) {
        Ticket ticket = ticketService.createTicket(sessionEvent, user);
        sendConfirmationOfPurchaseEmail(user,ticket);
    }

    private void sendConfirmationOfPurchaseEmail(User user, Ticket ticket) {
        String subject = "Confirmation of Purchase: " + ticket.getSession().getTitle();
        String htmlMessage = "<h1>Confirmation of Purchase</h1>" +
                "<p>Thank you for purchasing a ticket to " + ticket.getSession().getTitle() + ".</p>" +
                "<p>Ticket Code: <strong>" + ticket.getTicketCode() + "</strong></p>" +
                "<p>Please note that you will be required to provide this ticket code when checking in at the event.</p>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
