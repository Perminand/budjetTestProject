package ru.sevenwings.budget.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class BudgetDto {

    private Long id;

    @NotNull(message = "значение year не может быть null")
    @Positive(message = "значение year не может быть меньше или равно 0")
    private LocalDate year;

    @NotNull(message = "значение mount не может быть null")
    @Positive(message = "значение mount не может быть меньше или равно 0")
    private LocalDate mount;

    @NotNull(message = "значение amount не может быть null")
    @Positive(message = "значение amount не может быть меньше или равно 0")
    private int amount;

    @NotNull(message = "значение budgetType не может быть null")
    private BudgetType budgetType;
}
