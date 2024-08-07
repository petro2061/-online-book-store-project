package project.onlinebookstore.dto.shoppingcart;

import java.util.List;
import lombok.Data;
import project.onlinebookstore.dto.cartitem.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
