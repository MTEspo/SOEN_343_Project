package backend343.stripe;

import backend343.models.Event;
import backend343.models.User;
import backend343.models.UserEvent;
import backend343.repository.EventRepository;
import backend343.repository.UserEventRepository;
import backend343.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class StripeService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserEventRepository userEventRepository;

    public StripeResponse checkoutEvent(ProductRequest productRequest) {
        Event event = eventRepository.findById(productRequest.getEventId()).orElseThrow();
        User user = userRepository.findByEmail(productRequest.getUserEmail()).orElseThrow();

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(event.getName()).build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("CAD")
                        .setUnitAmount(event.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
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
                .build();

        Session session = null;
        try{
            session = Session.create(params);
        }catch(StripeException ex) {
            ex.printStackTrace();
        }

        if (session != null) {
            createUserEvent(event,user);
        }

        return StripeResponse.builder()
                .status("SUCCESS")
                .message("Payment session created successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    private void createUserEvent(Event event, User user){
        UserEvent userEvent = UserEvent.builder()
                .user(user)
                .event(event)
                .registrationDate(LocalDateTime.now())
                .build();
        userEventRepository.save(userEvent);
    }
}
