package ru.sevenwings.budget.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BudgetRecordDto(

        Long id,

        @NotNull(message = "значение year не может быть null")
        @Min(value = 1900, message = "значение year не может быть меньше 1900")
        @Max(value = 9999, message = "значение year не может быть больше 9999")
        Integer year,

        @NotNull(message = "значение month не может быть null")
        @Positive(message = "значение month не может быть меньше или равно 0")
        @Max(value = 12, message = "значение month не может быть больше 12")
        Integer month,

        @NotNull(message = "значение amount не может быть null")
        @Positive(message = "значение amount не может быть меньше или равно 0")
        Integer amount,

        @NotBlank(message = "Описание бюджета не может быть пустым")
        @Size(min = 3, max = 50)
        String budgetType) {
}
