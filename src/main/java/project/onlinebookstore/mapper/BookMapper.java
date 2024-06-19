package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.BookDto;
import project.onlinebookstore.dto.CreateBookRequestDto;
import project.onlinebookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBookModel(CreateBookRequestDto bookRequestDto);
}
