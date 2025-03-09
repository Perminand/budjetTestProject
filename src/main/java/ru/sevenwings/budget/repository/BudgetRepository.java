package ru.sevenwings.budget.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sevenwings.budget.model.BudgetRecord;

import java.util.List;

public interface BudgetRepository extends JpaRepository<BudgetRecord, Long> {

    @Query("SELECT b FROM BudgetRecord b WHERE b.year = :year")
    List<BudgetRecord> findAllByYear(@Param("year") Integer year, Pageable pageable);
}
