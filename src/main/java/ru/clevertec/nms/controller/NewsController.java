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
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    @PostMapping
    public ResponseEntity<NewsDtoResponse> createNews(@RequestBody NewsDtoRequest newsDtoRequest) {
        NewsDtoResponse savedNews = service.save(newsDtoRequest);
        return new ResponseEntity<>(savedNews, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NewsDtoResponse>> findAllNews(Pageable pageable) {
        List<NewsDtoResponse> newsDtoResponseList = service.findAll(pageable);
        return new ResponseEntity<>(newsDtoResponseList, HttpStatus.OK);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<NewsDtoResponse> findNewsById(@PathVariable Long id) {
        NewsDtoResponse newsDtoResponse = service.findById(id);
        return new ResponseEntity<>(newsDtoResponse, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NewsDtoResponse> updateNewsPartially(
            @PathVariable Long id,
            @RequestBody NewsDtoRequest newsDtoRequest
    ) {
        NewsDtoResponse giftCertificateUpdated = service.updateTittleAndTextById(id, newsDtoRequest);
        return new ResponseEntity<>(giftCertificateUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeNews(@PathVariable("id") Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
