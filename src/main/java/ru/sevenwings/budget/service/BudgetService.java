package ru.sevenwings.budget.service;

import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;

import java.util.List;

public interface BudgetService {
    BudgetDto create(BudgetDto budgetDto);

    List<BudgetDto> getBudget(BudgetParamForGetDto build);
}
