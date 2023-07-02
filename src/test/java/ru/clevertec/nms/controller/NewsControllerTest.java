package ru.clevertec.nms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.service.NewsService;
import ru.clevertec.nms.utils.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    NewsService newsService;

    @Nested
    class CreateNews {

        @Test
        void checkCreateNewsValidTest() throws Exception {
            NewsDtoRequest newsDtoRequest = TestData.buildNewsDtoRequest();
            NewsDtoResponse newsDtoResponse = TestData.buildNewsDtoResponse();
            when(newsService.save(newsDtoRequest))
                    .thenReturn(newsDtoResponse);

            mockMvc.perform(post("/api/news")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(objectMapper.writeValueAsString(newsDtoResponse)));
        }
    }

    @Nested
    class FindAllNews {

        @Test
        void checkFindAllNewsValidTest() throws Exception {
            List<NewsDtoResponse> newsDtoResponseList = List.of(TestData.buildNewsDtoResponse());
            when(newsService.findAll(any(Pageable.class)))
                    .thenReturn(newsDtoResponseList);

            mockMvc.perform(get("/api/news")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(newsDtoResponseList)));
        }

        @Test
        void checkFindAllNewsInvalidParamsWillNotFail() throws Exception {
            List<NewsDtoResponse> newsDtoResponseList = List.of(TestData.buildNewsDtoResponse());
            when(newsService.findAll(any(Pageable.class)))
                    .thenReturn(newsDtoResponseList);

            mockMvc.perform(get("/api/news")
                            .param("size", "five")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(newsDtoResponseList)));
        }
    }

    @Nested
    class FindNewsById {

        @Test
        void checkFindNewsByIdValidTest() throws Exception {
            long id = 1L;
            NewsDtoResponse newsDtoResponse = TestData.buildNewsDtoResponse();
            when(newsService.findById(id))
                    .thenReturn(newsDtoResponse);

            mockMvc.perform(get("/api/news/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(newsDtoResponse)));
        }

        @Test
        void checkFindNewsByIdWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(get("/api/news/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateNewsPartially {

        @Test
        void checkUpdateNewsPartiallyValidTest() throws Exception {
            long id = 1L;
            NewsDtoRequest newsDtoRequest = TestData.buildNewsDtoRequest();
            NewsDtoResponse newsDtoResponse = TestData.buildNewsDtoResponse();
            when(newsService.updateTittleAndTextById(id, newsDtoRequest))
                    .thenReturn(newsDtoResponse);

            mockMvc.perform(patch("/api/news/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newsDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(newsDtoResponse)));
        }

        @Test
        void checkUpdateNewsPartiallyWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(patch("/api/news/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class RemoveNews {

        @Test
        void checkRemoveNewsValidTest() throws Exception {

            mockMvc.perform(delete("/api/news/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void checkRemoveNewsWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(delete("/api/news/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
