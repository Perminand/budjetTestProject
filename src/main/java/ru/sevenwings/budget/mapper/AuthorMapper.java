package ru.sevenwings.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevenwings.budget.dto.AuthorDto;
import ru.sevenwings.budget.model.Author;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "createData", source = "createData")
    Author toEntity(AuthorDto authorDto, LocalDateTime createData);

    AuthorDto toDto(Author author);
}
