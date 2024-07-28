package project.onlinebookstore.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.book.BookDto;
import project.onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import project.onlinebookstore.dto.book.CreateBookRequestDto;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "categoriesIds", ignore = true)
    BookDto toBookDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toBookModel(CreateBookRequestDto bookRequestDto);

    void updateBookFromDto(CreateBookRequestDto book, @MappingTarget Book entity);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(Book book, @MappingTarget BookDto bookDto) {
        List<Long> categoriesIds = book.getCategories()
                .stream()
                .map(Category::getId)
                .toList();
        bookDto.setCategoriesIds(categoriesIds);
    }

    @AfterMapping
    default void setCategories(CreateBookRequestDto bookRequestDto, @MappingTarget Book book) {
        Set<Category> categories = bookRequestDto.getCategoriesIds()
                .stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Book::new)
                .orElse(null);
    }
}
