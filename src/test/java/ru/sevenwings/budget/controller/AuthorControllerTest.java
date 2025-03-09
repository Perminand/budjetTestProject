package ru.sevenwings.budget.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.sevenwings.budget.dto.AuthorDto;
import ru.sevenwings.budget.service.AuthorService;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @Test
    public void testCreateAuthorWithValidFio() throws Exception {
        String fio = "Иванов Иван Иванович";

        AuthorDto expectedAuthor = new AuthorDto(1L, fio, LocalDateTime.now());

        when(authorService.createAuthor(fio))
                .thenReturn(expectedAuthor);

        mockMvc.perform(post("/author/add")
                        .param("fio", fio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fio").value(fio));
    }

    @Test
    public void testCreateAuthorWithTooShortFio() throws Exception {
        String shortFio = "Ив";

        mockMvc.perform(post("/author/add")
                        .param("fio", shortFio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    @Test
    public void testCreateAuthorWithTooLongFio() throws Exception {
        String longFio = "a".repeat(256);

        mockMvc.perform(post("/author/add")
                        .param("fio", longFio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(jsonPath("$.statusCode").value(400));
    }


    @Test
    public void testCreateAuthorWithOnlySpaces() throws Exception {
        String spacesFio = "    ";

        mockMvc.perform(post("/author/add")
                        .param("fio", spacesFio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testCreateAuthorWithEmptyFio() throws Exception {
        String emptyFio = "";

        mockMvc.perform(post("/author/add")
                        .param("fio", emptyFio)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

}