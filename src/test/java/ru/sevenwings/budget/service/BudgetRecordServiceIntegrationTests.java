package ru.sevenwings.budget.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.request.BudgetParamForGetDto;
import ru.sevenwings.budget.dto.type.BudgetType;
import ru.sevenwings.budget.model.BudgetRecord;
import ru.sevenwings.budget.repository.BudgetRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BudgetRecordServiceIntegrationTests {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    void testCreate() {
        BudgetRecordDto dto = new BudgetRecordDto(null, 2023, 10, 5000, BudgetType.credit);
        BudgetRecordDto createdDto = budgetService.create(dto, authorId);
        assertThat(createdDto.getId()).isNotNull();
        assertThat(createdDto.getMount()).isEqualTo(10);
        assertThat(createdDto.getAmount()).isEqualTo(5000);
        assertThat(createdDto.getBudgetType()).isEqualTo(BudgetType.credit);
        Optional<BudgetRecord> foundInDb = budgetRepository.findById(createdDto.getId());
        assertThat(foundInDb.isPresent()).isTrue();
        assertThat(foundInDb.get().getMount()).isEqualTo(10);
        assertThat(foundInDb.get().getAmount()).isEqualTo(5000);
        assertThat(foundInDb.get().getBudgetType()).isEqualTo(BudgetType.credit);
    }

    @Test
    void testCreateWithInvalidMount() {
        BudgetRecordDto dto = new BudgetRecordDto(null, 2023, 13, 5000, BudgetType.debit);
        Throwable exception = catchThrowable(() -> budgetService.create(dto, authorId));
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(exception).hasMessageContaining("mount не может быть больше 12");
    }

    @Test
    void testCreateWithMaximumMountValue() {
        BudgetRecordDto dto = new BudgetRecordDto(null, 2023, 12, 5000, BudgetType.credit);
        BudgetRecordDto createdDto = budgetService.create(dto, authorId);
        assertThat(createdDto.getMount()).isEqualTo(12);
    }

    @Test
    void testCreateWithMinimumYearValue() {
        BudgetRecordDto dto = new BudgetRecordDto(null, 1, 12, 5000, BudgetType.debit);
        BudgetRecordDto createdDto = budgetService.create(dto, authorId);
        assertThat(createdDto.getYear()).isEqualTo(1);
    }

    @Test
    void testCreateWithMinimumAmountValue() {
        BudgetRecordDto dto = new BudgetRecordDto(null, 2023, 12, 1, BudgetType.credit);
        BudgetRecordDto createdDto = budgetService.create(dto, authorId);
        assertThat(createdDto.getAmount()).isEqualTo(1);
    }

    @Test
    void testGetBudget() {
        int year = 2023;
        BudgetRecordDto firstBudget = new BudgetRecordDto(null, year, 10, 7000, BudgetType.credit);
        BudgetRecordDto secondBudget = new BudgetRecordDto(null, year, 11, 9000, BudgetType.debit);
        BudgetRecordDto thirdBudget = new BudgetRecordDto(null, 2022, 12, 12000, BudgetType.credit);
        budgetService.create(firstBudget, authorId);
        budgetService.create(secondBudget, authorId);
        budgetService.create(thirdBudget, authorId);
        BudgetParamForGetDto param = new BudgetParamForGetDto(year);
        List<BudgetRecordDto> result = budgetService.getBudget(param);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).extracting(BudgetRecordDto::mount).containsExactly(10, 11);
        assertThat(result.get(0)).extracting(BudgetRecordDto::amount).containsExactly(7000, 9000);
        assertThat(result.get(0)).extracting(BudgetRecordDto::budgetType).containsExactly(BudgetType.credit, BudgetType.debit);
    }

    @Test
    void testEmptyResult() {
        int year = 2024;
        BudgetParamForGetDto param = new BudgetParamForGetDto(year);
        List<BudgetRecordDto> result = budgetService.getBudget(param);
        assertThat(result).isEmpty();
    }

    @Test
    void testInvalidPagination() {
        int year = 2023;
        BudgetParamForGetDto param = new BudgetParamForGetDto(year, -1, 10);
        assertThrows(IllegalArgumentException.class, () -> budgetService.getBudget(param));
    }

    @Test
    void testSorting() {
        int year = 2023;
        BudgetRecordDto firstBudget = new BudgetRecordDto(null, year, 3, 1500, BudgetType.credit);
        BudgetRecordDto secondBudget = new BudgetRecordDto(null, year, 6, 4000, BudgetType.debit);
        BudgetRecordDto thirdBudget = new BudgetRecordDto(null, year, 4, 2200, BudgetType.credit);
        budgetService.create(firstBudget, authorId);
        budgetService.create(secondBudget, authorId);
        budgetService.create(thirdBudget, authorId);
        BudgetParamForGetDto param = new BudgetParamForGetDto(year);
        List<BudgetRecordDto> result = budgetService.getBudget(param);
        assertThat(result).hasSize(3);
        assertThat(result.get(0).mount()).isEqualTo(3);
        assertThat(result.get(1).mount()).isEqualTo(4);
        assertThat(result.get(2).mount()).isEqualTo(6);
    }

    @Test
    void testIncorrectFilter() {
        BudgetParamForGetDto param = new BudgetParamForGetDto("abc");
        assertThrows(NumberFormatException.class, () -> budgetService.getBudget(param));
    }

    @Test
    void testOutOfBoundsPage() {
        int year = 2023;
        BudgetRecordDto firstBudget = new BudgetRecordDto(null, year, 8, 3500, BudgetType.credit);
        BudgetRecordDto secondBudget = new BudgetRecordDto(null, year, 9, 4700, BudgetType.debit);
        budgetService.create(firstBudget, authorId);
        budgetService.create(secondBudget, authorId);
        BudgetParamForGetDto param = new BudgetParamForGetDto(year, 1, 10);
        List<BudgetRecordDto> result = budgetService.getBudget(param);
        assertThat(result).isEmpty();
    }

    @Test
    void testZeroLimit() {
        int year = 2023;
        BudgetRecordDto firstBudget = new BudgetRecordDto(null, year, 7, 3750, BudgetType.credit);
        BudgetRecordDto secondBudget = new BudgetRecordDto(null, year, 8, 4250, BudgetType.debit);
        budgetService.create(firstBudget, authorId);
        budgetService.create(secondBudget, authorId);
        BudgetParamForGetDto param = new BudgetParamForGetDto(year, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> budgetService.getBudget(param));
    }

    @Test
    void testMultiplePages() {
        int year = 2023;
        for (int i = 1; i <= 20; i++) {
            BudgetRecordDto budget = new BudgetRecordDto(null, year, i, 500 * i, BudgetType.credit);
            budgetService.create(budget, authorId);
        }
        BudgetParamForGetDto paramFirstPage = new BudgetParamForGetDto(year);
        BudgetParamForGetDto paramSecondPage = new BudgetParamForGetDto(year, 1, 10);
        List<BudgetRecordDto> firstPage = budgetService.getBudget(paramFirstPage);
        List<BudgetRecordDto> secondPage = budgetService.getBudget(paramSecondPage);
        assertThat(firstPage).hasSize(10);
        assertThat(secondPage).hasSize(10);
        assertThat(firstPage.get(9).getMount()).isLessThan(secondPage.get(0).getMount());
    }
}