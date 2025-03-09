package ru.sevenwings.budget.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sevenwings.budget.dto.AuthorDto;
import ru.sevenwings.budget.mapper.AuthorMapper;
import ru.sevenwings.budget.model.Author;
import ru.sevenwings.budget.repository.AuthorRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public AuthorDto createAuthor(String fio) {
        Author author = authorMapper.toEntity(fio, LocalDateTime.now());
        authorRepository.save(author);
        return authorMapper.toDto(author);
    }
}
