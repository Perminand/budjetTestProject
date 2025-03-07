package ru.sevenwings.budget.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;
import ru.sevenwings.budget.mapper.BudgetMapper;
import ru.sevenwings.budget.model.Budget;
import ru.sevenwings.budget.repository.BudgetRepository;

import java.util.List;

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
        log.info("Создан budget {}", budget);
        return budgetMapper.toDto(budget);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetDto> getBudget(BudgetParamForGetDto build) {
        Pageable pageable = PageRequest.of(build.offset(), build.limit(), Sort.by("mount"));
        List<Budget> budgets = budgetRepository.findAllByYear(build.year(), pageable);
        List<BudgetDto> budgetDtos = budgets.stream().map(budgetMapper::toDto).toList();
        log.info("Вывод листа BudgetDto {}", budgetDtos);
        return budgetDtos;
    }
}
