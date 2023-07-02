package ru.clevertec.nms.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.nms.model.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentSearch {

    List<Comment> findByNewsId(long id, Pageable pageable);
}
