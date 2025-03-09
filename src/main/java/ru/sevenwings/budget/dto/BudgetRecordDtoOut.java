package ru.sevenwings.budget.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BudgetRecordDtoOut(

        Long id,

        Integer year,

        Integer month,

        Integer amount,

        String budgetType,

        String fio,

        LocalDateTime createRecord) {

}
