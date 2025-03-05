package ru.sevenwings.budget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.mapper.BudgetMapper;
import ru.sevenwings.budget.model.Budget;
import ru.sevenwings.budget.repository.BudgetRepository;

@Slf4j
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    @Override
    public BudgetDto create(BudgetDto budgetDto) {
        Budget budget = budgetMapper.toEntity(budgetDto);
        budget = budgetRepository.save(budget);
        return budgetMapper.toDto(budget);
    }
}
