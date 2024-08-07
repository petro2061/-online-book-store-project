package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.cartitem.CartItemDto;
import project.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import project.onlinebookstore.model.CartItem;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CreateCartItemRequestDto cartItemRequestDto);
}
