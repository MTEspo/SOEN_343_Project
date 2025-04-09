package backend343.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend343.models.Stakeholder;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {
    Optional<Stakeholder> findByEmail(String email);
}
