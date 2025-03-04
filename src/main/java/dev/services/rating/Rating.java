package dev.services.rating;

import dev.account.user.User;
import dev.core.common.AbstractAuditingEntity;
import dev.services.food.Food;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Nelson Tanko
 */
@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ratings", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "food_id"}))
public class Rating extends AbstractAuditingEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_sequence")
    @SequenceGenerator(name = "rating_sequence", sequenceName = "rating_sequence", allocationSize = 1, initialValue = 50)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private Integer rating;
}
