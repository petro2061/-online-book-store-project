package project.onlinebookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.onlinebookstore.config.MapperConfig;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;
import project.onlinebookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toCategoryModel(CreateCategoryRequestDto categoryRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(CreateCategoryRequestDto category,
                               @MappingTarget Category entity);
}
