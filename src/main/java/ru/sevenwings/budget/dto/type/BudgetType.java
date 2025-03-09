package ru.sevenwings.budget.dto.type;

import lombok.Getter;

@Getter
public enum BudgetType {
    credit("Расход"),
    debit("Приход");

    private final String description;

    BudgetType(String description) {
        this.description = description;
    }

    public static BudgetType fromDescription(String description) {
        for (BudgetType type : values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        return null;
    }
}
