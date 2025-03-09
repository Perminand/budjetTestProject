package ru.sevenwings.budget.service;

import ru.sevenwings.budget.dto.BudgetDtoOut;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.BudgetRecordDtoOut;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;

public interface BudgetService {
    BudgetRecordDtoOut create(BudgetRecordDto budgetRecordDto, Long authorId);

    BudgetDtoOut getBudget(BudgetParamForGetDto build);
}
