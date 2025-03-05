package ru.sevenwings.budget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ru.sevenwings.budget.dto.BudgetType;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;

    private int mount;

    private int amount;

    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;
}
