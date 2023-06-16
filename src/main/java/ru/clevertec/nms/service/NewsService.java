package ru.clevertec.nms.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.dto.NewsDtoRequest;
import ru.clevertec.nms.model.dto.NewsDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {

    NewsDtoResponse save(NewsDtoRequest newsDtoRequest);

    List<NewsDtoResponse> findAll(Pageable pageable);

    List<NewsDtoResponse> findAllBySearch(String searchText, LocalDateTime time, Pageable pageable);

    NewsDtoResponse findById(long id);

    NewsDtoResponse updateTittleAndTextById(long id, NewsDtoRequest newsDtoRequest);

    void deleteById(long id);
}
