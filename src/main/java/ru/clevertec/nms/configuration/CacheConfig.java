package ru.clevertec.nms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.nms.cache.Cache;
import ru.clevertec.nms.cache.impl.CacheFactoryImpl;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.model.dto.NewsDtoResponse;

@Configuration
public class CacheConfig {

    @Bean(name = "newsCache")
    public CacheFactoryImpl<Long, NewsDtoResponse> newsCacheFactory() {
        return new CacheFactoryImpl<>();
    }

    @Bean(name = "commentCache")
    public CacheFactoryImpl<Long, CommentDtoResponse> commentCacheFactory() {
        return new CacheFactoryImpl<>();
    }

    @Bean
    public Cache<Long, NewsDtoResponse> newsCache() throws Exception {
        return newsCacheFactory().getObject();
    }

    @Bean
    public Cache<Long, CommentDtoResponse> commentCache() throws Exception {
        return commentCacheFactory().getObject();
    }
}
