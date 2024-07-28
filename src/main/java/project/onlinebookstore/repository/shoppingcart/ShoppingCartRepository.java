package project.onlinebookstore.repository.shoppingcart;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.ShoppingCart;
import project.onlinebookstore.model.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, User> {
    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems "
            + "WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserId(Long userId);
}
