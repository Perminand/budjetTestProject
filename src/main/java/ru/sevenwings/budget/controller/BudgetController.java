package ru.sevenwings.budget.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;
import ru.sevenwings.budget.service.BudgetService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto createBudget(@RequestBody @Valid BudgetDto budgetDto) {
        log.info("Пришел POST запрос на сохранение Budget {}", budgetDto);
        return budgetService.create(budgetDto);
    }

    @GetMapping("/year/{year}/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<BudgetDto> getBudget(@PathVariable @Positive Integer year,
                                     @RequestParam(required = false, defaultValue = "10") @Positive Integer limit,
                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset) {
        log.info("Пришел GET запрос на получение информации year: {}, limit: {}, offset {}", year, limit, offset);
        return budgetService.getBudget(BudgetParamForGetDto.builder().year(year).limit(limit).offset(offset).build());
    }

}
