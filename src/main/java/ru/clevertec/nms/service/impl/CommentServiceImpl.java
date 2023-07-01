package ru.clevertec.nms.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.nms.annotation.CacheAlong;
import ru.clevertec.nms.annotation.Log;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.entity.Comment;
import ru.clevertec.nms.model.entity.News;
import ru.clevertec.nms.model.mapper.CommentMapper;
import ru.clevertec.nms.repository.CommentRepository;
import ru.clevertec.nms.repository.NewsRepository;
import ru.clevertec.nms.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;

    @Transactional
    @CacheAlong
    @Override
    public CommentDtoResponse save(CommentDtoRequest commentDtoRequest) {

        Comment comment = commentMapper.dtoRequestToEntity(commentDtoRequest);
        News news = newsRepository.findById(commentDtoRequest.getNewsId())
                .orElseThrow(() -> new NoSuchElementException("news with id " + commentDtoRequest.getNewsId() + " wasn't found"));
        ;
        comment.setTime(LocalDateTime.now());
        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.entityToDtoResponse(savedComment);
    }

    @Log
    @Override
    public List<CommentDtoResponse> findAll(Pageable pageable) {
        List<Comment> commentList = commentRepository.findAll(pageable).getContent();

        return commentList.stream().map(commentMapper::entityToDtoResponse).toList();
    }

    @CacheAlong
    @Override
    public CommentDtoResponse findById(long id) {
        CommentDtoResponse commentDtoResponse = commentRepository.findById(id)
                .map(commentMapper::entityToDtoResponse)
                .orElseThrow(() -> new NoSuchElementException("comment with id " + id + " wasn't found"));

        return commentDtoResponse;
    }

    @Override
    public List<CommentDtoResponse> findByNewsId(long id, Pageable pageable) {
        List<Comment> commentList = commentRepository.findByNewsId(id, pageable);

        return commentList.stream().map(commentMapper::entityToDtoResponse).toList();
    }

    @Override
    public List<CommentDtoResponse> findAllBySearch(String searchText, LocalDateTime time, Pageable pageable) {
        List<Comment> hits = commentRepository.searchCommentsByTextAndTime(searchText, time, pageable);

        return hits.stream().map(commentMapper::entityToDtoResponse).toList();
    }

    @Transactional
    @CacheAlong
    @Override
    public CommentDtoResponse updateTextById(long id, CommentDtoRequest commentDtoRequest) {
        Comment commentForUpdate = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("comment with id " + id + " wasn't found"));

        if (Objects.nonNull(commentDtoRequest.getText())) {
            commentForUpdate.setText(commentDtoRequest.getText());
        }

        Comment updatedComment = commentRepository.save(commentForUpdate);

        return commentMapper.entityToDtoResponse(updatedComment);
    }

    @Transactional
    @CacheAlong
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
