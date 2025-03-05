package ru.sevenwings.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevenwings.budget.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
