package ru.sevenwings.budget.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record BudgetDtoOut(
        Integer total,
        Map<String, Integer> totalByType,
        List<BudgetRecordDtoOut> items
) {

}
