package ru.clevertec.nms.model.mapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.News;
import ru.clevertec.nms.utils.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class NewsMapperTest {

    @Autowired
    private NewsMapper newsMapper;

    @Nested
    class EntityToDtoResponse {

        @Test
        void checkEntityToDtoResponse() {
            News news = TestData.buildNews();
            NewsDtoResponse expectedNewsDtoResponse = TestData.buildNewsDtoResponse();

            NewsDtoResponse actualNewsDtoResponse = newsMapper.entityToDtoResponse(news);

            assertThat(actualNewsDtoResponse).isEqualTo(expectedNewsDtoResponse);
        }
    }

    @Nested
    class DtoRequestToEntity {

        @Test
        void checkDtoRequestToEntity() {
            NewsDtoRequest newsDtoRequest = TestData.buildNewsDtoRequest();
            News expectedNews = TestData.buildNews();

            News actualNews = newsMapper.dtoRequestToEntity(newsDtoRequest);

            assertAll(
                    () -> assertThat(actualNews.getText()).isEqualTo(expectedNews.getText()),
                    () -> assertThat(actualNews.getTitle()).isEqualTo(expectedNews.getTitle())
            );
        }
    }
}
