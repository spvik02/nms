package ru.clevertec.nms.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.model.entity.News;
import ru.clevertec.nms.model.mapper.NewsMapper;
import ru.clevertec.nms.repository.NewsRepository;
import ru.clevertec.nms.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;
    private final NewsMapper mapper;

    @Transactional
    @Override
    public NewsDtoResponse save(NewsDtoRequest newsDtoRequest) {
        News news = mapper.dtoRequestToEntity(newsDtoRequest);
        news.setTime(LocalDateTime.now());

        News savedNews = repository.save(news);

        return mapper.entityToDtoResponse(savedNews);
    }

    @Override
    public List<NewsDtoResponse> findAll(Pageable pageable) {
        List<News> newsList = repository.findAll(pageable).getContent();

        return newsList.stream().map(mapper::entityToDtoResponse).toList();
    }

    @Override
    public List<NewsDtoResponse> findAllBySearch(String searchText, LocalDateTime time, Pageable pageable) {
        List<News> hits = repository.searchNewsByTextAndTime(searchText, time, pageable);

        return hits.stream().map(mapper::entityToDtoResponse).toList();
    }


    @Override
    public NewsDtoResponse findById(long id) {
        NewsDtoResponse newsDtoResponse = repository.findById(id)
                .map(mapper::entityToDtoResponse)
                .orElseThrow(() -> new NoSuchElementException("news with id " + id + " wasn't found"));

        return newsDtoResponse;
    }

    @Transactional
    @Override
    public NewsDtoResponse updateTittleAndTextById(long id, NewsDtoRequest newsDtoRequest) {
        News newsForUpdate = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("news with id " + id + " wasn't found"));

        if (Objects.nonNull(newsDtoRequest.getTitle())) {
            newsForUpdate.setTitle(newsDtoRequest.getTitle());
        }
        if (Objects.nonNull(newsDtoRequest.getText())) {
            newsForUpdate.setText(newsDtoRequest.getText());
        }

        News updatedNews = repository.save(newsForUpdate);

        return mapper.entityToDtoResponse(updatedNews);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
