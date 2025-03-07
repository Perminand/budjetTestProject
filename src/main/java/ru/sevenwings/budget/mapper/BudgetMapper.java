package ru.sevenwings.budget.mapper;

import org.mapstruct.Mapper;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.model.Budget;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    Budget toEntity(BudgetDto budgetDto);

    BudgetDto toDto(Budget budget);
}
