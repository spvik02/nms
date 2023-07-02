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
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.service.CommentService;
import ru.clevertec.nms.utils.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @Nested
    class CreateComment {

        @Test
        void checkCreateCommentValidTest() throws Exception {
            CommentDtoRequest commentDtoRequest = TestData.buildCommentDtoRequest();
            CommentDtoResponse commentDtoResponse = TestData.buildCommentDtoResponse();
            when(commentService.save(commentDtoRequest))
                    .thenReturn(commentDtoResponse);

            mockMvc.perform(post("/api/comments")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentDtoResponse)));
        }
    }

    @Nested
    class FindAllComments {

        @Test
        void checkFindAllCommentsValidTest() throws Exception {
            List<CommentDtoResponse> commentsDtoResponseList = List.of(TestData.buildCommentDtoResponse());
            when(commentService.findAll(any(Pageable.class)))
                    .thenReturn(commentsDtoResponseList);

            mockMvc.perform(get("/api/comments")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentsDtoResponseList)));
        }

        @Test
        void checkFindAllCommentsInvalidParamsWillNotFail() throws Exception {
            List<CommentDtoResponse> commentDtoResponseList = List.of(TestData.buildCommentDtoResponse());
            when(commentService.findAll(any(Pageable.class)))
                    .thenReturn(commentDtoResponseList);

            mockMvc.perform(get("/api/comments")
                            .param("size", "five")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentDtoResponseList)));
        }
    }

    @Nested
    class FindAllCommentsByNewsId {

        @Test
        void checkFindAllCommentsByNewsIdValidTest() throws Exception {
            List<CommentDtoResponse> commentsDtoResponseList = List.of(TestData.buildCommentDtoResponse());
            when(commentService.findByNewsId(anyLong(), any(Pageable.class)))
                    .thenReturn(commentsDtoResponseList);

            mockMvc.perform(get("/api/comments/news/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentsDtoResponseList)));
        }

        @Test
        void checkFindAllCommentsByNewsIdInvalidParamsWillNotFail() throws Exception {
            List<CommentDtoResponse> commentDtoResponseList = List.of(TestData.buildCommentDtoResponse());
            when(commentService.findByNewsId(anyLong(), any(Pageable.class)))
                    .thenReturn(commentDtoResponseList);

            mockMvc.perform(get("/api/comments/news/1")
                            .param("size", "five")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentDtoResponseList)));
        }
    }

    @Nested
    class FindCommentById {

        @Test
        void checkFindCommentByIdValidTest() throws Exception {
            long id = 1L;
            CommentDtoResponse commentDtoResponse = TestData.buildCommentDtoResponse();
            when(commentService.findById(id))
                    .thenReturn(commentDtoResponse);

            mockMvc.perform(get("/api/comments/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentDtoResponse)));
        }

        @Test
        void checkFindCommentByIdWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(get("/api/comments/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdateCommentPartially {

        @Test
        void checkUpdateCommentPartiallyValidTest() throws Exception {
            long id = 1L;
            CommentDtoRequest commentDtoRequest = TestData.buildCommentDtoRequest();
            CommentDtoResponse commentDtoResponse = TestData.buildCommentDtoResponse();
            when(commentService.updateTextById(id, commentDtoRequest))
                    .thenReturn(commentDtoResponse);

            mockMvc.perform(patch("/api/comments/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(commentDtoRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(commentDtoResponse)));
        }

        @Test
        void checkUpdateCommentPartiallyWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(patch("/api/comments/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class RemoveComment {

        @Test
        void checkRemoveCommentValidTest() throws Exception {

            mockMvc.perform(delete("/api/comments/1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void checkRemoveCommentWithMethodArgumentTypeMismatchEndWithBadRequest() throws Exception {

            mockMvc.perform(delete("/api/comments/one")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
