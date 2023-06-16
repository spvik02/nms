package ru.clevertec.nms.repository;

import org.springframework.data.domain.Pageable;
import ru.clevertec.nms.model.entity.News;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsSearch {
    List<News> searchNewsByTextAndTime(String searchText, LocalDateTime time, Pageable pageable);
}
