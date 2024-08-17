package project.onlinebookstore.mapper;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.order.CreateOrderRequestDto;
import project.onlinebookstore.dto.order.CreateOrderUpdateRequestDto;
import project.onlinebookstore.dto.order.OrderDto;
import project.onlinebookstore.model.CartItem;
import project.onlinebookstore.model.Order;
import project.onlinebookstore.model.OrderItem;
import project.onlinebookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "orderDate", dateFormat = "yyyy-MM-dd HH:mm")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "cart.user")
    @Mapping(target = "total", source = "cart.cartItems", qualifiedByName = "getTotalPrice")
    @Mapping(target = "orderItems", source = "cart.cartItems", qualifiedByName = "toOrderItems")
    @Mapping(target = "shippingAddress", source = "orderRequestDto.shippingAddress")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    Order toModel(ShoppingCart cart, CreateOrderRequestDto orderRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateOrderStatus(CreateOrderUpdateRequestDto updateRequestDto,
                           @MappingTarget Order order);

    @Named("getTotalPrice")
    default BigDecimal getTotalPrice(Set<CartItem> cartItems) {
        return cartItems
                .stream()
                .map(orderItem ->
                        orderItem.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Named("toOrderItems")
    default Set<OrderItem> toOrderItems(Set<CartItem> cartItems) {
        return cartItems
                .stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }
}
