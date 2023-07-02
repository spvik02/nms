package ru.clevertec.nms.testcontainer.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.service.CommentService;
import ru.clevertec.nms.testcontainer.BaseTest;
import ru.clevertec.nms.utils.TestData;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class CommentServiceImplTest extends BaseTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void checkSaveShouldReturnNewsWithNotNullId() {
        CommentDtoRequest commentDtoRequest = TestData.buildCommentDtoRequest();

        CommentDtoResponse actualComment = commentService.save(commentDtoRequest);

        assertThat(actualComment.getId()).isNotNull();
    }

    @Test
    void checkFindAllShouldReturn20() {
        int expectedCount = 20;

        List<CommentDtoResponse> actualCommentList = commentService.findAll(Pageable.ofSize(50));

        assertThat(actualCommentList).hasSize(expectedCount);
    }

    @Test
    void checkFindByNewsIdShouldReturn10() {
        int expectedCount = 10;

        List<CommentDtoResponse> actualCommentList = commentService.findByNewsId(1, Pageable.ofSize(50));

        assertThat(actualCommentList).hasSize(expectedCount);
    }

    @Test
    void checkFindAllGiftCertificatesPageableShouldReturn2() {
        int expectedCount = 2;

        List<CommentDtoResponse> actualCommentList = commentService.findAll(Pageable.ofSize(2));

        assertThat(actualCommentList).hasSize(expectedCount);
    }

    @Test
    void checkFindByIdShouldReturnCommentWithId1() {
        long expectedId = 1;

        CommentDtoResponse actualComment = commentService.findById(1L);

        assertThat(actualComment.getId()).isEqualTo(expectedId);
    }


    @Test
    void checkDeleteByIdShouldNotFindCommentByDeletedId() {
        long expectedId = 1;

        commentService.deleteById(expectedId);
        entityManager.flush();

        assertThatThrownBy(() -> {
            commentService.findById(expectedId);
        }).isInstanceOf(NoSuchElementException.class);
    }
}
