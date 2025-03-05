package ru.sevenwings.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.model.Budget;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "id", ignore = true)
    Budget toEntity(BudgetDto budgetDto);

    BudgetDto toDto(Budget budget);
}
