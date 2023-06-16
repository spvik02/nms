package ru.clevertec.nms.repository.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.nms.model.entity.News;
import ru.clevertec.nms.repository.NewsSearch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsSearchImpl implements NewsSearch {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<News> searchNewsByTextAndTime(String searchText, LocalDateTime time, Pageable pageable) {

        SearchSession searchSession = Search.session(entityManager);
        SearchResult<News> result = searchSession.search(News.class)
                .where(news -> news.bool(b -> {
                    b.must(news.matchAll());
                    if (Objects.nonNull(searchText)) {
                        b.must(news.match()
                                .field("title").boost(2.0f)
                                .field("text").boost(1.1f)
                                .matching(searchText));
                    }
                    if (Objects.nonNull(time)) {
                        b.filter(news.range()
                                .field("time")
                                .atLeast(time));
                    }
                }))
                .sort(SearchSortFactory::score)
                .fetch(pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());

        return result.hits();
    }
}
