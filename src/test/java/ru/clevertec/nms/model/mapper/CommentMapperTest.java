package ru.clevertec.nms.model.mapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.entity.Comment;
import ru.clevertec.nms.utils.TestData;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper mapper;

    @Nested
    class EntityToDtoResponse {

        @Test
        void checkEntityToDtoResponse() {
            Comment comment = TestData.buildComment();
            CommentDtoResponse expectedCommentDtoResponse = TestData.buildCommentDtoResponse();

            CommentDtoResponse actualCommentDtoResponse = mapper.entityToDtoResponse(comment);

            assertThat(actualCommentDtoResponse).isEqualTo(expectedCommentDtoResponse);
        }
    }

    @Nested
    class DtoRequestToEntity {

        @Test
        void checkDtoRequestToEntity() {
            CommentDtoRequest commentDtoRequest = TestData.buildCommentDtoRequest();
            Comment expectedComment = TestData.buildComment();

            Comment actualComment = mapper.dtoRequestToEntity(commentDtoRequest);

            System.out.println(actualComment);
            System.out.println(expectedComment);

            assertAll(
                    () -> assertThat(actualComment.getText()).isEqualTo(expectedComment.getText()),
                    () -> assertThat(actualComment.getUsername()).isEqualTo(expectedComment.getUsername())
            );
        }
    }
}
