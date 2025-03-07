package ru.sevenwings.budget.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sevenwings.budget.model.Budget;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.year = :year")
    List<Budget> findAllByYear(@Param("year") Integer year, Pageable pageable);
}
