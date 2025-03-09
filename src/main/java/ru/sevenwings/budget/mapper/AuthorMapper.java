package ru.sevenwings.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevenwings.budget.dto.AuthorDto;
import ru.sevenwings.budget.model.Author;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "fio", source = "fio")
    @Mapping(target = "createData", source = "createData")
    Author toEntity(String fio, LocalDateTime createData);

    AuthorDto toDto(Author author);
}
