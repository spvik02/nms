package ru.clevertec.nms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService service;

    @PostMapping
    public ResponseEntity<CommentDtoResponse> createComment(@RequestBody CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse savedComment = service.save(commentDtoRequest);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDtoResponse>> findAllComments(Pageable pageable) {
        List<CommentDtoResponse> commentDtoResponseList = service.findAll(pageable);
        return new ResponseEntity<>(commentDtoResponseList, HttpStatus.OK);
    }

    @GetMapping("/news/{id}")
    public ResponseEntity<List<CommentDtoResponse>> findAllCommentsByNewsId(@PathVariable Long id, Pageable pageable) {
        List<CommentDtoResponse> commentDtoResponseList = service.findByNewsId(id, pageable);
        return new ResponseEntity<>(commentDtoResponseList, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentDtoResponse>> findCommentsBySearch(
            Pageable pageable,
            @RequestParam(value = "searchText", required = false) String searchText,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(value = "time", required = false) LocalDateTime time
    ) {

        List<CommentDtoResponse> commentDtoResponseList = service.findAllBySearch(searchText, time, pageable);
        return new ResponseEntity<>(commentDtoResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDtoResponse> findCommentById(@PathVariable Long id) {
        CommentDtoResponse commentDtoResponse = service.findById(id);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommentDtoResponse> updateCommentPartially(
            @PathVariable Long id,
            @RequestBody CommentDtoRequest commentDtoRequest
    ) {
        CommentDtoResponse commentUpdated = service.updateTextById(id, commentDtoRequest);
        return new ResponseEntity<>(commentUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeComment(@PathVariable("id") Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
