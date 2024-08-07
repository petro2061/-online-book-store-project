package project.onlinebookstore.repository.shoppingcart;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.ShoppingCart;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems "
            + "LEFT JOIN FETCH sc.cartItems.book "
            + "WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserId(Long userId);
}
