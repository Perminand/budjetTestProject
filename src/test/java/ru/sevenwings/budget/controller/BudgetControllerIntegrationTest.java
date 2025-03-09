package ru.sevenwings.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.sevenwings.budget.dto.BudgetDtoOut;
import ru.sevenwings.budget.dto.BudgetRecordDto;
import ru.sevenwings.budget.dto.BudgetRecordDtoOut;
import ru.sevenwings.budget.service.BudgetService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = BudgetController.class)
public class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BudgetService budgetService;

    @Test
    public void testCreateBudgetWithValidData() throws Exception {
        int year = 2023;
        int month = 10;
        int amount = 10000;
        String budgetType = "Расход";

        BudgetRecordDto requestDto = BudgetRecordDto.builder()
                .year(year)
                .month(month)
                .amount(amount)
                .budgetType(budgetType)
                .build();

        when(budgetService.create(
                argThat(param ->
                        param.year() == year &&
                                param.month() == month &&
                                param.amount() == amount &&
                                param.budgetType().equals(budgetType)),
                any()))
                .thenReturn(BudgetRecordDtoOut.builder()
                        .year(year)
                        .month(month)
                        .amount(amount)
                        .budgetType(budgetType)
                        .build());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/budget/add")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.year").value(year))
                .andExpect(jsonPath("$.month").value(month))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.budgetType").value(budgetType));
    }


    @Test
    public void testInvalidAuthor() throws Exception {
        int year = 2023;
        int month = 10;
        int amount = 10000;
        String budgetType = "Расходы";

        BudgetRecordDto requestDto = BudgetRecordDto.builder()
                .year(year)
                .month(month)
                .amount(amount)
                .budgetType(budgetType)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/budget/add")
                        .param("authorId", "-1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidYear() throws Exception {
        int invalidYear = 10000;
        int month = 10;
        int amount = 10000;
        String budgetType = "Расходы";

        BudgetRecordDto requestDto = BudgetRecordDto.builder()
                .year(invalidYear)
                .month(month)
                .amount(amount)
                .budgetType(budgetType)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/budget/add")
                        .param("authorId", "123") // Передаем верный ID автора
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShortBudgetType() throws Exception {
        int year = 2023;
        int month = 10;
        int amount = 10000;
        String shortBudgetType = "Р";

        BudgetRecordDto requestDto = BudgetRecordDto.builder()
                .year(year)
                .month(month)
                .amount(amount)
                .budgetType(shortBudgetType)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/budget/add")
                        .param("authorId", "123")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLongBudgetType() throws Exception {
        int year = 2023;
        int month = 10;
        int amount = 10000;
        String longBudgetType = "Это очень длинное описание бюджета, которое превышает установленное ограничение в 50 символов"; // Слишком длинная строка

        BudgetRecordDto requestDto = BudgetRecordDto.builder()
                .year(year)
                .month(month)
                .amount(amount)
                .budgetType(longBudgetType)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/budget/add")
                        .param("authorId", "123")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetBudgetWithValidData() throws Exception {
        BudgetRecordDtoOut record1 = BudgetRecordDtoOut.builder()
                .id(1L)
                .year(2023)
                .month(10)
                .amount(5000)
                .budgetType("Расходы")
                .build();

        BudgetRecordDtoOut record2 = BudgetRecordDtoOut.builder()
                .id(2L)
                .year(2023)
                .month(11)
                .amount(3000)
                .budgetType("Доходы")
                .build();

        Map<String, Integer> totalByType = new HashMap<>();
        totalByType.put("Расходы", 5000);
        totalByType.put("Доходы", 3000);

        BudgetDtoOut expectedResult = BudgetDtoOut.builder()
                .total(8000)
                .totalByType(totalByType)
                .items(List.of(record1, record2))
                .build();

        when(budgetService.getBudget(
                argThat(param -> param.year() == 2023 &&
                        param.limit() == 10 &&
                        param.offset() == 0 &&
                        param.search().equals("test"))))
                .thenReturn(expectedResult);

        mockMvc.perform(get("/budget/year/2023/stats")
                        .param("limit", "10")
                        .param("offset", "0")
                        .param("search", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(8000))
                .andExpect(jsonPath("$.totalByType.Расходы").value(5000))
                .andExpect(jsonPath("$.totalByType.Доходы").value(3000))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id").value(1L))
                .andExpect(jsonPath("$.items[0].year").value(2023))
                .andExpect(jsonPath("$.items[0].amount").value(5000))
                .andExpect(jsonPath("$.items[0].budgetType").value("Расходы"));
    }

    @Test
    public void testGetBudgetWhenNoRecordsFound() throws Exception {
        when(budgetService.getBudget(argThat(param -> param.year() == 2023 && param.limit() == 10 && param.offset() == 0)))
                .thenReturn(BudgetDtoOut.builder()
                        .total(0)
                        .totalByType(Map.of())
                        .items(Collections.emptyList())
                        .build());

        mockMvc.perform(get("/budget/year/2023/stats")
                        .param("limit", "10")
                        .param("offset", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.totalByType").isEmpty())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    public void testGetBudgetWithMaxLimit() throws Exception {
        List<BudgetRecordDtoOut> records = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> BudgetRecordDtoOut.builder()
                        .id((long) i)
                        .year(2023)
                        .month(10)
                        .amount(5000)
                        .budgetType("Расходы")
                        .build())
                .collect(Collectors.toList());

        Map<String, Integer> totalByType = new HashMap<>();
        totalByType.put("Расходы", 50000);

        BudgetDtoOut expectedResult = BudgetDtoOut.builder()
                .total(50000)
                .totalByType(totalByType)
                .items(records)
                .build();

        when(budgetService.getBudget(argThat(param -> param.year() == 2023 && param.limit() == 10 && param.offset() == 0)))
                .thenReturn(expectedResult);

        mockMvc.perform(get("/budget/year/2023/stats")
                        .param("limit", "10")
                        .param("offset", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(50000))
                .andExpect(jsonPath("$.totalByType.Расходы").value(50000))
                .andExpect(jsonPath("$.items", hasSize(10)))
                .andExpect(jsonPath("$.items[0].id").value(1L))
                .andExpect(jsonPath("$.items[0].year").value(2023))
                .andExpect(jsonPath("$.items[0].amount").value(5000))
                .andExpect(jsonPath("$.items[0].budgetType").value("Расходы"));
    }

    @Test
    public void testGetBudgetWithOffset() throws Exception {
        List<BudgetRecordDtoOut> records = IntStream.rangeClosed(8, 10)
                .mapToObj(i -> BudgetRecordDtoOut.builder()
                        .id((long) i)
                        .year(2023)
                        .month(10)
                        .amount(5000)
                        .budgetType("Расходы")
                        .build())
                .collect(Collectors.toList());

        Map<String, Integer> totalByType = new HashMap<>();
        totalByType.put("Расходы", 15000);

        BudgetDtoOut expectedResult = BudgetDtoOut.builder()
                .total(15000)
                .totalByType(totalByType)
                .items(records)
                .build();

        when(budgetService.getBudget(argThat(param -> param.year() == 2023 && param.limit() == 3 && param.offset() == 7)))
                .thenReturn(expectedResult);

        mockMvc.perform(get("/budget/year/2023/stats")
                        .param("limit", "3")
                        .param("offset", "7"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(15000))
                .andExpect(jsonPath("$.totalByType.Расходы").value(15000))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].id").value(8L))
                .andExpect(jsonPath("$.items[0].year").value(2023))
                .andExpect(jsonPath("$.items[0].amount").value(5000))
                .andExpect(jsonPath("$.items[0].budgetType").value("Расходы"));
    }

    @Test
    public void testGetBudgetWithMinimalParameters() throws Exception {
        when(budgetService.getBudget(argThat(param -> param.year() == 1900 && param.limit() == 1 && param.offset() == 0)))
                .thenReturn(BudgetDtoOut.builder()
                        .total(5000)
                        .totalByType(Map.of("Расходы", 5000))
                        .items(List.of(BudgetRecordDtoOut.builder()
                                .id(1L)
                                .year(1900)
                                .month(1)
                                .amount(5000)
                                .budgetType("Расходы")
                                .build()))
                        .build());

        mockMvc.perform(get("/budget/year/1900/stats")
                        .param("limit", "1")
                        .param("offset", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(5000))
                .andExpect(jsonPath("$.totalByType.Расходы").value(5000))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id").value(1L))
                .andExpect(jsonPath("$.items[0].year").value(1900))
                .andExpect(jsonPath("$.items[0].amount").value(5000))
                .andExpect(jsonPath("$.items[0].budgetType").value("Расходы"));
    }
}