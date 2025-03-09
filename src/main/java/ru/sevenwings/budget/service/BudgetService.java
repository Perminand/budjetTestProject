package ru.sevenwings.budget.service;

import ru.sevenwings.budget.dto.BudgetDtoOut;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;

public interface BudgetService {
    BudgetRecordDto create(BudgetRecordDto budgetRecordDto, Long authorId);

    BudgetDtoOut getBudget(BudgetParamForGetDto build);
}
