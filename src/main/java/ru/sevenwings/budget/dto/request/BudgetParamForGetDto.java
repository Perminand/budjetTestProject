package ru.sevenwings.budget.dto.request;

import lombok.Builder;

@Builder
public record BudgetParamForGetDto(
        Integer year,
        Integer limit,
        Integer offset

) {
}
