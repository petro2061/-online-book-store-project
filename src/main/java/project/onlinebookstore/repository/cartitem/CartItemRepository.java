package project.onlinebookstore.repository.cartitem;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.onlinebookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci "
            + "LEFT JOIN FETCH ci.book "
            + "WHERE ci.shoppingCart.id = :shoppingCartId AND ci.book.id = :bookId")
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
