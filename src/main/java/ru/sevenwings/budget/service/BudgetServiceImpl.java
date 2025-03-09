package ru.sevenwings.budget.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sevenwings.budget.dto.BudgetDtoOut;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.BudgetRecordDtoOut;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;
import ru.sevenwings.budget.dto.type.BudgetType;
import ru.sevenwings.budget.mapper.BudgetMapper;
import ru.sevenwings.budget.model.BudgetRecord;
import ru.sevenwings.budget.repository.AuthorRepository;
import ru.sevenwings.budget.repository.BudgetRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    private final AuthorRepository authorRepository;

    private final BudgetMapper budgetMapper;

    @Override
    public BudgetRecordDtoOut create(BudgetRecordDto budgetRecordDto, Long authorId) {
        BudgetType budgetType = BudgetType.fromDescription(budgetRecordDto.budgetType());

        if (budgetType == null) {
            throw new IllegalArgumentException("Неизвестный тип бюджета: " + budgetRecordDto.budgetType());
        }

        BudgetRecord budgetRecord = budgetMapper.toEntity(budgetRecordDto, budgetType);
        if (authorId != null) {
            budgetRecord.setAuthor(authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("author не найден id = " + authorId)));
            budgetRecord.setCreateRecord(LocalDateTime.now());
        }
        budgetRecord = budgetRepository.save(budgetRecord);
        log.info("Создан budget {}", budgetRecord);
        return budgetMapper.toDtoOut(budgetRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetDtoOut getBudget(BudgetParamForGetDto build) {
        Pageable pageable = PageRequest.of(build.offset(), build.limit(), Sort.by("mount"));
        List<BudgetRecord> budgetRecords;
        if (build.search() != null) {
            budgetRecords = budgetRepository.findAllByYearAndFio(build.year(), build.search().toLowerCase(), pageable);
        } else {
            budgetRecords = budgetRepository.findAllByYear(build.year(), pageable);
        }
        int total = 0;
        Map<String, Integer> totalByType = new HashMap<>();
        for (BudgetRecord br : budgetRecords) {
            total += br.getAmount();
            String description = br.getBudgetType().getDescription();
            if (!totalByType.containsKey(description)) {
                totalByType.put(description, br.getAmount());
            } else {
                totalByType.replace(description, totalByType.get(description) + br.getAmount());
            }
        }
        List<BudgetRecordDtoOut> budgetRecordDtos = budgetRecords.stream().map(budgetMapper::toDtoOut).toList();
        log.info("Вывод листа BudgetDto {}", budgetRecordDtos);
        return BudgetDtoOut.builder().total(total).totalByType(totalByType).items(budgetRecordDtos).build();
    }
}
