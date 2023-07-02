package ru.clevertec.nms.testcontainer.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.service.NewsService;
import ru.clevertec.nms.testcontainer.BaseTest;
import ru.clevertec.nms.utils.TestData;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class NewsServiceImplTest extends BaseTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void checkSaveShouldReturnNewsWithNotNullId() {
        NewsDtoRequest newsDtoRequest = TestData.buildNewsDtoRequest();

        NewsDtoResponse actualNews = newsService.save(newsDtoRequest);

        assertThat(actualNews.getId()).isNotNull();
    }

    @Test
    void checkFindAllShouldReturn3() {
        int expectedCount = 2;

        List<NewsDtoResponse> actualNewsList = newsService.findAll(Pageable.ofSize(5));

        assertThat(actualNewsList).hasSize(expectedCount);
    }

    @Test
    void checkFindAllGiftCertificatesPageableShouldReturn2() {
        int expectedCount = 2;

        List<NewsDtoResponse> actualCertificates = newsService.findAll(Pageable.ofSize(2));

        assertThat(actualCertificates).hasSize(expectedCount);
    }

    @Test
    void checkFindByIdShouldReturnNewsWithId1() {
        long expectedId = 1;

        NewsDtoResponse actualNews = newsService.findById(1L);

        assertThat(actualNews.getId()).isEqualTo(expectedId);
    }

    @Test
    void checkDeleteByIdShouldNotFindGiftCertificateByDeletedId() {
        long expectedId = 1;

        newsService.deleteById(expectedId);

        entityManager.flush();

        assertThatThrownBy(() -> {
            newsService.findById(expectedId);
        }).isInstanceOf(NoSuchElementException.class);
    }
}
