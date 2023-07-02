package ru.clevertec.nms.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.News;
import ru.clevertec.nms.model.mapper.NewsMapper;
import ru.clevertec.nms.repository.NewsRepository;
import ru.clevertec.nms.service.NewsService;
import ru.clevertec.nms.utils.TestData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = {NewsServiceImpl.class})
class NewsServiceImplTest {

    private News news;
    private NewsDtoRequest newsDtoRequest;
    private NewsDtoResponse newsDtoResponse;

    @Autowired
    private NewsService newsService;

    @MockBean
    private NewsRepository repository;

    @MockBean
    private NewsMapper mapper;

    @Captor
    private ArgumentCaptor<News> argumentCaptor;

    @BeforeEach
    void setUp() {
        news = TestData.buildNews();
        newsDtoRequest = TestData.buildNewsDtoRequest();
        newsDtoResponse = TestData.buildNewsDtoResponse();
    }

    @Nested
    class Save {
        @Test
        void checkSaveShouldSetTime() {
            news.setId(null);
            news.setTime(null);

            when(mapper.dtoRequestToEntity(any(NewsDtoRequest.class)))
                    .thenReturn(news);
            when(repository.save(any(News.class)))
                    .thenReturn(news);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.save(newsDtoRequest);
            verify(repository).save(argumentCaptor.capture());
            News newsCapturedValue = argumentCaptor.getValue();

            assertNotNull(newsCapturedValue.getTime());
        }

        @Test
        void checkSaveShouldInvokeRepositoryMethod() {
            when(mapper.dtoRequestToEntity(any(NewsDtoRequest.class)))
                    .thenReturn(news);
            when(repository.save(any(News.class)))
                    .thenReturn(news);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.save(newsDtoRequest);

            verify(repository, times(1)).save(any(News.class));
        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldInvokeRepositoryMethod() {
            List<News> newsList = List.of(news);
            Page<News> newsPage = new PageImpl<>(newsList);

            when(repository.findAll(any(Pageable.class)))
                    .thenReturn(newsPage);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.findAll(Pageable.ofSize(3));

            verify(repository, times(1)).findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindAllBySearch {

        @Test
        void checkFindAllBySearchShouldInvokeRepositoryMethod() {
            List<News> newsList = List.of(news);

            when(repository.searchNewsByTextAndTime(any(String.class), any(LocalDateTime.class), any(Pageable.class)))
                    .thenReturn(newsList);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.findAllBySearch("text", LocalDateTime.now(), Pageable.ofSize(3));

            verify(repository, times(1))
                    .searchNewsByTextAndTime(any(String.class), any(LocalDateTime.class), any(Pageable.class));
        }
    }

    @Nested
    class FindById {

        @Test
        void checkFindByIdShouldThrowNoSuchElementException() {
            doReturn(Optional.empty()).when(repository).findById(1L);

            assertThatThrownBy(() -> {
                newsService.findById(1L);
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void checkFindByIdInvokeRepositoryMethod() {
            when(repository.findById(1L))
                    .thenReturn(Optional.of(news));
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.findById(1L);

            verify(repository, times(1)).findById(1L);
        }
    }

    @Nested
    class UpdateTittleAndTextById {

        @Test
        void checkUpdateTittleAndTextByIdShouldThrowNoSuchElementException() {
            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                newsService.updateTittleAndTextById(1L, newsDtoRequest);
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void checkUpdateTittleAndTextByIdShouldUpdateOnlyTittleField() {
            String updatedField = "updated field";
            NewsDtoRequest dtoWithUpdateData = NewsDtoRequest.builder()
                    .title(updatedField)
                    .build();
            News foundedNews = TestData.buildNews();
            News updatedNews = TestData.buildNews();
            NewsDtoResponse expectedUpdatedNewsDtoResponse = TestData.buildNewsDtoResponse();
            updatedNews.setTitle(updatedField);
            expectedUpdatedNewsDtoResponse.setTitle(updatedField);

            when(repository.findById(1L))
                    .thenReturn(Optional.of(foundedNews));
            when(repository.save(any(News.class)))
                    .thenReturn(updatedNews);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(expectedUpdatedNewsDtoResponse);

            newsService.updateTittleAndTextById(1L, dtoWithUpdateData);
            verify(repository).save(argumentCaptor.capture());
            News updatedNewsForSaveArgumentCaptorValue = argumentCaptor.getValue();

            assertAll(
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getTitle()).isEqualTo(updatedField),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getId()).isEqualTo(foundedNews.getId()),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getText()).isEqualTo(foundedNews.getText()),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getTime()).isEqualTo(foundedNews.getTime())
            );
        }

        @Test
        void checkUpdateTittleAndTextByIdShouldUpdateOnlyTextField() {
            String updatedField = "updated field";
            NewsDtoRequest dtoWithUpdateData = NewsDtoRequest.builder()
                    .text(updatedField)
                    .build();
            News updatedNews = TestData.buildNews();
            updatedNews.setText(updatedField);
            newsDtoResponse.setText(updatedField);

            when(repository.findById(1L))
                    .thenReturn(Optional.of(news));
            when(repository.save(any(News.class)))
                    .thenReturn(updatedNews);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.updateTittleAndTextById(1L, dtoWithUpdateData);
            verify(repository).save(argumentCaptor.capture());
            News updatedNewsForSaveArgumentCaptorValue = argumentCaptor.getValue();

            assertAll(
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getTitle()).isEqualTo(news.getTitle()),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getId()).isEqualTo(news.getId()),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getText()).isEqualTo(updatedField),
                    () -> assertThat(updatedNewsForSaveArgumentCaptorValue.getTime()).isEqualTo(news.getTime())
            );
        }

        @Test
        void checkUpdateTittleAndTextByIdInvokeRepositoryMethods() {
            String updatedField = "updated field";
            NewsDtoRequest dtoWithUpdateData = NewsDtoRequest.builder()
                    .text(updatedField)
                    .build();
            News updatedNews = TestData.buildNews();

            updatedNews.setText(updatedField);
            newsDtoResponse.setText(updatedField);

            when(repository.findById(1L))
                    .thenReturn(Optional.of(news));
            when(repository.save(any(News.class)))
                    .thenReturn(updatedNews);
            when(mapper.entityToDtoResponse(any(News.class)))
                    .thenReturn(newsDtoResponse);

            newsService.updateTittleAndTextById(1L, dtoWithUpdateData);

            verify(repository, times(1)).findById(1L);
            verify(repository, times(1)).save(any(News.class));
        }

        @Test
        void checkUpdateTittleAndTextByIdShouldNotInvokeRepositorySaveMethodIfNotFound() {

            String updatedField = "updated field";
            NewsDtoRequest dtoWithUpdateData = NewsDtoRequest.builder()
                    .text(updatedField)
                    .build();
            News updatedNews = TestData.buildNews();
            updatedNews.setText(updatedField);
            newsDtoResponse.setText(updatedField);

            when(repository.findById(1L))
                    .thenReturn(Optional.empty());

            try {
                newsService.updateTittleAndTextById(1L, dtoWithUpdateData);
            } catch (Exception ignored) {

            }

            verify(repository, times(1)).findById(1L);
            verify(repository, times(0)).save(any(News.class));
        }

    }

    @Nested
    class DeleteById {
        @Test
        void checkDeleteByIdInvokeRepositoryMethod() {
            doNothing().when(repository).deleteById(anyLong());

            newsService.deleteById(1L);

            verify(repository, times(1)).deleteById(1L);
        }
    }
}
