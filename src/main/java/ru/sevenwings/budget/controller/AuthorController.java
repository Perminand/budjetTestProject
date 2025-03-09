package ru.sevenwings.budget.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sevenwings.budget.dto.AuthorDto;
import ru.sevenwings.budget.service.AuthorService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto createAuthor(@Size(min = 5, max = 255) @RequestParam String fio) {
        log.info("Пришел POST запрос на создание author: {}", fio);
        return authorService.createAuthor(fio);
    }
}
