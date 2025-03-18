package backend343.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend343.models.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{
    Session findByStripeProductId(String stripeProductId);
}
