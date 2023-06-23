package ru.clevertec.nms.service.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class BuildSearchIndexService implements ApplicationListener<ApplicationReadyEvent> {

    @PersistenceContext
    private final EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.search.backend.directory.need-reindex}")
    boolean isNeedIndexing;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        if (isNeedIndexing) {
            try {
                SearchSession searchSession = Search.session(entityManager);
                MassIndexer indexer = searchSession.massIndexer()
                        .threadsToLoadObjects(5);
                indexer.startAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}