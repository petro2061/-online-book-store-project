package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.orderitem.OrderItemDto;
import project.onlinebookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

}
