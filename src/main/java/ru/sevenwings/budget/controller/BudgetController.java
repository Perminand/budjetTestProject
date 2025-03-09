package ru.sevenwings.budget.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sevenwings.budget.dto.BudgetDtoOut;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.BudgetRecordDtoOut;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;
import ru.sevenwings.budget.service.BudgetService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
@Tag(name = "Контроллер бюджета", description = "Конечные точки для управления бюджетом")
public class BudgetController {

    private final BudgetService budgetService;


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавление новой записи бюджета", description = "Создание новой записи бюджета с указанными параметрами")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запись бюджета успешно создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BudgetRecordDtoOut.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    public BudgetRecordDtoOut createBudget(@RequestBody(description = "Объект записи бюджета, который нужно добавить в базу данных", required = true) @Valid BudgetRecordDto budgetRecordDto,
                                           @RequestParam(required = false) @Positive Long authorId) {
        log.info("Пришел POST запрос на сохранение Budget {}, автор: {}", budgetRecordDto, authorId);
        return budgetService.create(budgetRecordDto, authorId);
    }

    @GetMapping("/year/{year}/stats")
    @ResponseStatus(HttpStatus.OK)
    public BudgetDtoOut getBudget(@PathVariable @Min(1900) Integer year,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer limit,
                                  @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
                                  @RequestParam(required = false) String search) {
        log.info("Пришел GET запрос на получение информации year: {}, limit: {}, offset {}", year, limit, offset);
        return budgetService.getBudget(BudgetParamForGetDto.builder()
                .year(year)
                .limit(limit)
                .offset(offset)
                .search(search)
                .build()
        );
    }

}
