package ru.sevenwings.budget.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.type.BudgetType;
import ru.sevenwings.budget.model.BudgetRecord;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "budgetType", source = "budgetType")
    BudgetRecord toEntity(BudgetRecordDto budgetRecordDto, BudgetType budgetType);

    @Mapping(target = "budgetType", source = "budgetRecord.budgetType.description")
    BudgetRecordDto toDto(BudgetRecord budgetRecord);
}
