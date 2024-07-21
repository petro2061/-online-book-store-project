package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;
import project.onlinebookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategoryModel(CreateCategoryRequestDto categoryRequestDto);

    void updateCategoryFromDto(CreateCategoryRequestDto category,
                               @MappingTarget Category entity);
}
