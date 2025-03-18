package backend343.stripe;

import backend343.models.Session;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import com.stripe.model.Product;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeProductService {

    public String createStripeProduct(Session session) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", session.getTitle());
        params.put("description", session.getSchedule().getEvent().getDescription());

        Product product = Product.create(params);
        return product.getId();
    }
}
