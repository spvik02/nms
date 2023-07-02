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
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * News API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    /**
     * Creates new News with provided data.
     *
     * @param newsDtoRequest object with data for News
     * @return ResponseEntity with saved News
     */
    @Operation(summary = "Creates news with provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created News")
    })
    @PostMapping
    public ResponseEntity<NewsDtoResponse> createNews(@RequestBody NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse savedNews = service.save(newsDtoRequest);
        return new ResponseEntity<>(savedNews, HttpStatus.CREATED);
    }

    /**
     * Returns list of News within the specified range
     *
     * @param pageable abstract interface for pagination information
     * @return ResponseEntity with list of News
     */
    @Operation(summary = "Returns list of News within the specified range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping
    public ResponseEntity<List<NewsDtoResponse>> findAllNews(Pageable pageable) {
        List<NewsDtoResponse> newsDtoResponseList = service.findAll(pageable);
        return new ResponseEntity<>(newsDtoResponseList, HttpStatus.OK);
    }

    /**
     * Returns list of News satisfying the requirements within the range specified by Pageable.
     * Order elements by their relevance score. Full-text search is provided on tittle and text fields.
     *
     * @param text     text for full-text search on field tittle and text
     * @param time     date_time for set time range where time of news publication will be greater than or equal to this time
     * @param pageable abstract interface for pagination information
     * @return ResponseEntity with list of News
     */
    @Operation(summary = "Returns list of News satisfying the requirements (full-text search or time filter)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/search")
    public ResponseEntity<List<NewsDtoResponse>> findNewsBySearch(
            @RequestParam(value = "text", required = false)
            String text,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(value = "time", required = false)
            LocalDateTime time,
            Pageable pageable
    ) {

        List<NewsDtoResponse> newsDtoResponseList = service.findAllBySearch(text, time, pageable);
        return new ResponseEntity<>(newsDtoResponseList, HttpStatus.OK);
    }

    /**
     * Returns News with provided id
     *
     * @param id news id for search
     * @return ResponseEntity with newsDtoResponse
     */
    @Operation(summary = "Returns News with provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "News Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDtoResponse> findNewsById(@PathVariable Long id) {
        NewsDtoResponse newsDtoResponse = service.findById(id);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.OK);
    }

    /**
     * Updates News with data passed in request body (title and text are available for update).
     * If field is not specified in request body the data is not updated.
     *
     * @param id             id of News that should be updated
     * @param newsDtoRequest request body containing News data
     * @return ResponseEntity with newsDtoResponse containing updated News data
     */
    @Operation(summary = "Updates News")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<NewsDtoResponse> updateNewsPartially(
            @PathVariable Long id,
            @RequestBody NewsDtoRequest newsDtoRequest
    ) {
        NewsDtoResponse giftCertificateUpdated = service.updateTittleAndTextById(id, newsDtoRequest);
        return new ResponseEntity<>(giftCertificateUpdated, HttpStatus.OK);
    }

    /**
     * Deletes News with passed id.
     *
     * @param id news id
     * @return ResponseEntity with status
     */
    @Operation(summary = "Deletes News")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeNews(@PathVariable("id") Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
