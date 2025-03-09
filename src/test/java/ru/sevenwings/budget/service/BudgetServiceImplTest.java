package ru.sevenwings.budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.BudgetRecordDtoOut;
import ru.sevenwings.budget.dto.type.BudgetType;
import ru.sevenwings.budget.mapper.BudgetMapper;
import ru.sevenwings.budget.model.Author;
import ru.sevenwings.budget.model.BudgetRecord;
import ru.sevenwings.budget.repository.AuthorRepository;
import ru.sevenwings.budget.repository.BudgetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    public void testCreate_Success() {
        // Подготовка данных
        BudgetRecordDto budgetRecordDto = BudgetRecordDto.builder()
                .budgetType("Приход")
                .amount(1000)
                .month(12)
                .year(2025)
                .build();

        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);

        BudgetRecord budgetRecord = new BudgetRecord();
        budgetRecord.setId(1L);
        BudgetRecordDtoOut expectedDto = BudgetRecordDtoOut.builder()
                .budgetType("Приход")
                .amount(1000)
                .month(12)
                .year(2025)
                .build();

        // Настройка моков
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(budgetMapper.toEntity(budgetRecordDto, BudgetType.DEBIT)).thenReturn(budgetRecord);
        when(budgetRepository.save(budgetRecord)).thenReturn(budgetRecord);
        when(budgetMapper.toDtoOut(budgetRecord)).thenReturn(expectedDto);

        // Выполнение
        BudgetRecordDtoOut result = budgetService.create(budgetRecordDto, authorId);

        // Проверки
        assertEquals(expectedDto, result);
        verify(budgetRepository).save(budgetRecord);
        verify(budgetMapper).toEntity(budgetRecordDto, BudgetType.DEBIT);
        verify(budgetMapper).toDtoOut(budgetRecord);
    }

    @Test
    public void testCreate_UnknownBudgetType() {
        // Подготовка данных
        BudgetRecordDto budgetRecordDto = BudgetRecordDto.builder()
                .budgetType("Неизвестный тип")
                .amount(1000)
                .month(12)
                .year(2025)
                .build();

        // Выполнение и проверка
        assertThrows(IllegalArgumentException.class,
                () -> budgetService.create(budgetRecordDto, 1L));
    }


    @Test
    public void testCreate_NullAuthorId() {
        // Подготовка данных
        BudgetRecordDto budgetRecordDto = BudgetRecordDto.builder()
                .budgetType("Расход")
                .amount(500)
                .month(12)
                .year(2025)
                .build();

        BudgetRecord budgetRecord = new BudgetRecord();
        budgetRecord.setId(1L);
        BudgetRecordDtoOut expectedDto = BudgetRecordDtoOut.builder()
                .id(1L)
                .budgetType("Расход")
                .amount(500)
                .month(12)
                .year(2025)
                .build();

        // Настройка моков
        when(budgetMapper.toEntity(budgetRecordDto, BudgetType.CREDIT)).thenReturn(budgetRecord);
        when(budgetRepository.save(budgetRecord)).thenReturn(budgetRecord);
        when(budgetMapper.toDtoOut(budgetRecord)).thenReturn(expectedDto);

        // Выполнение
        BudgetRecordDtoOut result = budgetService.create(budgetRecordDto, null);

        // Проверки
        assertEquals(expectedDto, result);
        verify(budgetRepository).save(budgetRecord);
        verify(budgetMapper).toEntity(budgetRecordDto, BudgetType.CREDIT);
        verify(budgetMapper).toDtoOut(budgetRecord);
    }
}