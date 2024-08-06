package project.onlinebookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import project.onlinebookstore.model.Order;
import project.onlinebookstore.model.OrderItem;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItem> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
