package project.onlinebookstore.repository.orderitem;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order "
            + "LEFT JOIN FETCH oi.book WHERE oi.order.user.id = :userId "
            + "AND oi.order.id =:orderId")
    List<OrderItem> getAllOrderItemsByOrderId(Long userId, Long orderId);

    @Query("SELECT oi FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE oi.order.user.id = :userId "
            + "AND oi.order.id =:orderId "
            + "AND oi.id = :orderItemId")
    Optional<OrderItem> getOrderItemByOrderIdAndOrderItemId(
            Long userId,
            Long orderId,
            Long orderItemId);
}
