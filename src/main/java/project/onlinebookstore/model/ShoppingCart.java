package project.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Setter
@Getter
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = true WHERE id =?")
@SQLRestriction(value = "is_deleted = false")
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private User user;
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems = new HashSet<>();
    @Column(nullable = false)
    private boolean isDeleted = false;
}