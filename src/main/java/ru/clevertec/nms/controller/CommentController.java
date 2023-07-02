package ru.clevertec.nms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import ru.clevertec.nms.annotation.Log;
import ru.clevertec.nms.model.dto.CommentDtoRequest;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Comment API
 */
@Log
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService service;

    /**
     * Creates new Comment with provided data.
     *
     * @param commentDtoRequest object with data for Comment
     * @return ResponseEntity with saved Comment
     */
    @Operation(summary = "Creates Comment with provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created Comment")
    })
    @PostMapping
    public ResponseEntity<CommentDtoResponse> createComment(@RequestBody CommentDtoRequest commentDtoRequest) {
        CommentDtoResponse savedComment = service.save(commentDtoRequest);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    /**
     * Returns list of Comments within the specified range
     *
     * @param pageable abstract interface for pagination information
     * @return ResponseEntity with list of Comments
     */
    @Operation(summary = "Returns list of Comments within the specified range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<List<CommentDtoResponse>> findAllComments(Pageable pageable) {
        List<CommentDtoResponse> commentDtoResponseList = service.findAll(pageable);
        return new ResponseEntity<>(commentDtoResponseList, HttpStatus.OK);
    }

    /**
     * Returns list of Comments of specified News within the specified range
     *
     * @param id       news id for which find comments
     * @param pageable abstract interface for pagination information
     * @return ResponseEntity with list of Comments of specified News
     */
    @Operation(summary = "Returns list of Comments of specified News within the specified range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/news/{id}")
    public ResponseEntity<List<CommentDtoResponse>> findAllCommentsByNewsId(@PathVariable Long id, Pageable pageable) {
        List<CommentDtoResponse> commentDtoResponseList = service.findByNewsId(id, pageable);
        return new ResponseEntity<>(commentDtoResponseList, HttpStatus.OK);
    }

    /**
     * Returns list of Comments satisfying the requirements within the range specified by Pageable.
     * Order elements by their relevance score. Full-text search is provided on text and username fields.
     *
     * @param pageable   abstract interface for pagination information
     * @param searchText text for full-text search on field tittle and text
     * @param time       date_time for set time range where time of news publication will be greater than or equal to this time
     * @return ResponseEntity with list of Comments
     */
    @Operation(summary = "Returns list of Comments satisfying the requirements (full-text search or time filter)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
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

    /**
     * Returns Comment with provided id
     *
     * @param id comment id for search
     * @return ResponseEntity with found comment
     */
    @Operation(summary = "Returns News with provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Comment Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentDtoResponse> findCommentById(@PathVariable Long id) {
        CommentDtoResponse commentDtoResponse = service.findById(id);
        return new ResponseEntity<>(commentDtoResponse, HttpStatus.OK);
    }

    /**
     * Updates Comment with data passed in request body (text are available for update).
     * If field is not specified in request body the data is not updated.
     *
     * @param id                id of comment that should be updated
     * @param commentDtoRequest request body containing comment data
     * @return ResponseEntity with CommentDtoResponse containing updated comment data
     */
    @Operation(summary = "Updates Comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDtoResponse> updateCommentPartially(
            @PathVariable Long id,
            @RequestBody CommentDtoRequest commentDtoRequest
    ) {
        CommentDtoResponse commentUpdated = service.updateTextById(id, commentDtoRequest);
        return new ResponseEntity<>(commentUpdated, HttpStatus.OK);
    }

    /**
     * Deletes Comment with passed id.
     *
     * @param id comment id
     * @return ResponseEntity with status
     */
    @Operation(summary = "Deletes Comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeComment(@PathVariable("id") Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
