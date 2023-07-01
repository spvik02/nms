package ru.clevertec.nms.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.annotation.Log;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDtoResponse save(CommentDtoRequest commentDtoRequest);

    List<CommentDtoResponse> findAll(Pageable pageable);

    List<CommentDtoResponse> findByNewsId(long id, Pageable pageable);

    List<CommentDtoResponse> findAllBySearch(String searchText, LocalDateTime time, Pageable pageable);

    CommentDtoResponse findById(long id);

    CommentDtoResponse updateTextById(long id, CommentDtoRequest CommentDtoRequest);

    void deleteById(long id);
}
