package ru.sevenwings.budget.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.sevenwings.budget.dto.BudgetDto;
import ru.sevenwings.budget.dto.type.BudgetType;
import ru.sevenwings.budget.mapper.BudgetMapper;
import ru.sevenwings.budget.repository.BudgetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;


    @Test
    void testCreateBudget_success() throws Exception {
        BudgetDto validDto = BudgetDto.builder()
                .year(2025)
                .mount(3)
                .amount(1000)
                .budgetType(BudgetType.debit)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(validDto);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/budget/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        BudgetDto responseDto = objectMapper.readValue(responseContent, BudgetDto.class);

        assertNotNull(responseDto.id());
        assertEquals(validDto.year(), responseDto.year());
        assertEquals(validDto.mount(), responseDto.mount());
        assertEquals(validDto.amount(), responseDto.amount());
        assertEquals(validDto.budgetType(), responseDto.budgetType());
    }

    @Test
    void testCreateBudget_invalidYear() throws Exception {
        BudgetDto invalidYearDto = BudgetDto.builder()
                .year(0)
                .mount(3)
                .amount(1000)
                .budgetType(BudgetType.debit)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(invalidYearDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/budget/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBudget_invalidMount() throws Exception {
        BudgetDto invalidMountDto = BudgetDto.builder()
                .year(2025)
                .mount(0)
                .amount(1000)
                .budgetType(BudgetType.debit)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(invalidMountDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/budget/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBudget_invalidAmount() throws Exception {
        BudgetDto invalidAmountDto = BudgetDto.builder()
                .year(2025)
                .mount(3)
                .amount(0)
                .budgetType(BudgetType.debit)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(invalidAmountDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/budget/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBudget_missingFields() throws Exception {
        BudgetDto missingFieldsDto = BudgetDto.builder()
                .year(2025)
                .mount(3)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(missingFieldsDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/budget/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBudget_success() throws Exception {
        budgetRepository.deleteAll();

        List<BudgetDto> testData = generateBudgetData(2025, 20);
        testData.forEach(budgetDto -> budgetRepository.save(budgetMapper.toEntity(budgetDto)));


        int year = 2025;

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/budget/year/{year}/stats", year)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<BudgetDto> responseDtos = objectMapper.readValue(responseContent, new TypeReference<List<BudgetDto>>() {
        });

        assertNotNull(responseDtos);
        assertFalse(responseDtos.isEmpty());
        responseDtos.forEach(dto -> assertEquals(year, dto.year()));
    }

    @Test
    void testGetBudget_invalidYear() throws Exception {
        // Проверка на некорректный год
        int invalidYear = 0;

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/budget/year/{year}/stats", invalidYear)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetBudget_limitAndOffset() throws Exception {

        int year = 2025;
        int limit = 5;
        int offset = 10;

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/budget/year/{year}/stats", year)
                                .param("limit", String.valueOf(limit))
                                .param("offset", String.valueOf(offset))
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<BudgetDto> responseDtos = objectMapper.readValue(responseContent, new TypeReference<List<BudgetDto>>() {
        });

        assertNotNull(responseDtos);
        assertTrue(responseDtos.size() <= limit);
    }

    @Test
    void testGetBudget_noData() throws Exception {

        int year = 2026;

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/budget/year/{year}/stats", year)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<BudgetDto> responseDtos = objectMapper.readValue(responseContent, new TypeReference<List<BudgetDto>>() {
        });

        assertNotNull(responseDtos);
        assertTrue(responseDtos.isEmpty());
    }

    private List<BudgetDto> generateBudgetData(int year, int count) {
        List<BudgetDto> budgetData = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            int month = random.nextInt(12) + 1;
            int amount = random.nextInt(1000) + 1;
            BudgetType budgetType = random.nextBoolean() ? BudgetType.debit : BudgetType.debit;

            BudgetDto budgetDto = BudgetDto.builder()
                    .year(year)
                    .mount(month)
                    .amount(amount)
                    .budgetType(budgetType)
                    .build();

            budgetData.add(budgetDto);
        }

        return budgetData;
    }
}
