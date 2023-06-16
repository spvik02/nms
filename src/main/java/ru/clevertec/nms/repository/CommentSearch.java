package ru.clevertec.nms.repository;

import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentSearch {

    List<Comment> searchCommentsByTextAndTime(String searchText, LocalDateTime time, Pageable pageable);
}
