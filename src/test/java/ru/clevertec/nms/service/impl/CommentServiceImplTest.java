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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.entity.Comment;
import ru.clevertec.nms.model.mapper.CommentMapper;
import ru.clevertec.nms.repository.CommentRepository;
import ru.clevertec.nms.repository.NewsRepository;
import ru.clevertec.nms.service.CommentService;
import ru.clevertec.nms.utils.TestData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CommentServiceImpl.class})
class CommentServiceImplTest {

    private Comment comment;
    private CommentDtoRequest commentDtoRequest;
    private CommentDtoResponse commentDtoResponse;

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private CommentMapper commentMapper;

    @Captor
    private ArgumentCaptor<Comment> argumentCaptor;

    @BeforeEach
    void setUp() {
        comment = TestData.buildComment();
        commentDtoRequest = TestData.buildCommentDtoRequest();
        commentDtoResponse = TestData.buildCommentDtoResponse();
    }

    @Nested
    class Save {

        @Test
        void checkSaveShouldSetTime() {
            comment.setId(null);
            comment.setTime(null);

            when(commentMapper.dtoRequestToEntity(any(CommentDtoRequest.class)))
                    .thenReturn(comment);
            when(newsRepository.findById(1L))
                    .thenReturn(Optional.of(TestData.buildNews()));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(comment);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.save(commentDtoRequest);
            verify(commentRepository).save(argumentCaptor.capture());
            Comment commentCapturedValue = argumentCaptor.getValue();

            assertNotNull(commentCapturedValue.getTime());
        }

        @Test
        void checkSaveShouldInvokeRepositoryMethod() {
            when(commentMapper.dtoRequestToEntity(any(CommentDtoRequest.class)))
                    .thenReturn(comment);
            when(newsRepository.findById(1L))
                    .thenReturn(Optional.of(TestData.buildNews()));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(comment);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.save(commentDtoRequest);

            verify(commentRepository, times(1)).save(any(Comment.class));
        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldInvokeRepositoryMethod() {
            List<Comment> commentList = List.of(comment);
            Page<Comment> commentPage = new PageImpl<>(commentList);

            when(commentRepository.findAll(any(Pageable.class)))
                    .thenReturn(commentPage);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.findAll(Pageable.ofSize(3));

            verify(commentRepository, times(1)).findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindByNewsId {

        @Test
        void checkFindByNewsIdShouldInvokeRepositoryMethod() {
            List<Comment> commentList = List.of(comment);

            when(commentRepository.findByNewsId(anyLong(), any(Pageable.class)))
                    .thenReturn(commentList);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.findByNewsId(1L, Pageable.ofSize(3));

            verify(commentRepository, times(1)).findByNewsId(anyLong(), any(Pageable.class));
        }
    }

    @Nested
    class FindAllBySearch {

        @Test
        void checkFindAllBySearchShouldInvokeRepositoryMethod() {
            List<Comment> newsList = List.of(comment);

            when(commentRepository.searchCommentsByTextAndTime(any(String.class), any(LocalDateTime.class), any(Pageable.class)))
                    .thenReturn(newsList);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.findAllBySearch("text", LocalDateTime.now(), Pageable.ofSize(3));

            verify(commentRepository, times(1))
                    .searchCommentsByTextAndTime(any(String.class), any(LocalDateTime.class), any(Pageable.class));
        }
    }

    @Nested
    class FindById {

        @Test
        void checkFindByIdShouldThrowNoSuchElementException() {
            doReturn(Optional.empty()).when(commentRepository).findById(1L);

            assertThatThrownBy(() -> {
                commentService.findById(1L);
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void checkFindByIdInvokeRepositoryMethod() {
            when(commentRepository.findById(1L))
                    .thenReturn(Optional.of(comment));
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.findById(1L);

            verify(commentRepository, times(1)).findById(1L);
        }
    }

    @Nested
    class UpdateTittleAndTextById {

        @Test
        void checkUpdateTittleAndTextByIdShouldThrowNoSuchElementException() {
            when(commentRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                commentService.updateTextById(1L, commentDtoRequest);
            }).isInstanceOf(NoSuchElementException.class);
        }

        @Test
        void checkUpdateTittleAndTextByIdShouldUpdateOnlyTextField() {
            String updatedField = "updated field";
            CommentDtoRequest dtoWithUpdateData = CommentDtoRequest.builder()
                    .text(updatedField)
                    .build();
            Comment foundedComment = TestData.buildComment();
            Comment updatedComment = TestData.buildComment();
            CommentDtoResponse expectedUpdatedCommentDtoResponse = TestData.buildCommentDtoResponse();
            updatedComment.setText(updatedField);
            expectedUpdatedCommentDtoResponse.setText(updatedField);

            when(commentRepository.findById(1L))
                    .thenReturn(Optional.of(foundedComment));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(updatedComment);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(expectedUpdatedCommentDtoResponse);

            commentService.updateTextById(1L, dtoWithUpdateData);
            verify(commentRepository).save(argumentCaptor.capture());
            Comment updatedCommentForSaveArgumentCaptorValue = argumentCaptor.getValue();

            assertAll(
                    () -> assertThat(updatedCommentForSaveArgumentCaptorValue.getText()).isEqualTo(updatedField),
                    () -> assertThat(updatedCommentForSaveArgumentCaptorValue.getId()).isEqualTo(foundedComment.getId()),
                    () -> assertThat(updatedCommentForSaveArgumentCaptorValue.getUsername()).isEqualTo(foundedComment.getUsername()),
                    () -> assertThat(updatedCommentForSaveArgumentCaptorValue.getTime()).isEqualTo(foundedComment.getTime())
            );
        }

        @Test
        void checkUpdateTittleAndTextByIdInvokeRepositoryMethods() {
            String updatedField = "updated field";
            CommentDtoRequest dtoWithUpdateData = CommentDtoRequest.builder()
                    .text(updatedField)
                    .build();
            Comment updatedComment = TestData.buildComment();

            updatedComment.setText(updatedField);
            commentDtoResponse.setText(updatedField);

            when(commentRepository.findById(1L))
                    .thenReturn(Optional.of(comment));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(updatedComment);
            when(commentMapper.entityToDtoResponse(any(Comment.class)))
                    .thenReturn(commentDtoResponse);

            commentService.updateTextById(1L, dtoWithUpdateData);

            verify(commentRepository, times(1)).findById(1L);
            verify(commentRepository, times(1)).save(any(Comment.class));
        }

        @Test
        void checkUpdateTittleAndTextByIdShouldNotInvokeRepositorySaveMethodIfNotFound() {

            String updatedField = "updated field";
            CommentDtoRequest dtoWithUpdateData = CommentDtoRequest.builder()
                    .text(updatedField)
                    .build();
            Comment updatedComment = TestData.buildComment();

            updatedComment.setText(updatedField);
            commentDtoResponse.setText(updatedField);

            when(commentRepository.findById(1L))
                    .thenReturn(Optional.empty());

            try {
                commentService.updateTextById(1L, dtoWithUpdateData);
            } catch (Exception ignored) {

            }

            verify(commentRepository, times(1)).findById(1L);
            verify(commentRepository, times(0)).save(any(Comment.class));
        }

    }

    @Nested
    class DeleteById {
        @Test
        void checkDeleteByIdInvokeRepositoryMethod() {
            doNothing().when(commentRepository).deleteById(anyLong());

            commentService.deleteById(1L);

            verify(commentRepository, times(1)).deleteById(1L);
        }
    }
}
