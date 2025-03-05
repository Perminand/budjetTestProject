package ru.sevenwings.budget.dto;

import lombok.Getter;

@Getter
public enum BudgetType {
    credit("Расход"),
    debit("Приход");

    private final String description;

    BudgetType(String description) {
        this.description = description;
    }

}
