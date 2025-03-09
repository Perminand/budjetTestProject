package ru.sevenwings.budget.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record BudgetRecordDto(

        Long id,

        @NotNull(message = "значение year не может быть null")
        @Positive(message = "значение year не может быть меньше или равно 0")
        Integer year,

        @NotNull(message = "значение mount не может быть null")
        @Positive(message = "значение mount не может быть меньше или равно 0")
        @Max(value = 12, message = "значение mount не может быть больше 12")
        Integer mount,

        @NotNull(message = "значение amount не может быть null")
        @Positive(message = "значение amount не может быть меньше или равно 0")
        Integer amount,

        @NotBlank(message = "Описание бюджета не может быть пустым")
        String budgetType) {
}
