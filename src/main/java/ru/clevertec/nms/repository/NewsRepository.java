package ru.clevertec.nms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.nms.model.entity.News;

public interface NewsRepository extends JpaRepository<News, Long>, NewsSearch {

}
