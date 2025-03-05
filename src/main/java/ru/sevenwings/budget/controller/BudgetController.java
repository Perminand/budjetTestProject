package ru.sevenwings.budget.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.service.BudgetService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetDto createBudget(@RequestBody BudgetDto budgetDto) {
        log.info("Пришел POST запрос на сохранение Budget {}", budgetDto);
        return budgetService.create(budgetDto);
    }
}
