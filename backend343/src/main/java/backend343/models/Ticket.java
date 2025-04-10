package backend343.models;

import backend343.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Session session;

    private LocalDateTime registrationDate;

    private String stripePaymentId;

    private String ticketCode;
    private Boolean isCodeUsed;

    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
