package ru.clevertec.nms.aop.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.nms.cache.Cache;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.util.CacheUtils;

/**
 * AOP for work with News cache
 */
@Profile("cache")
@Aspect
@Component
@RequiredArgsConstructor
public class NewsCacheAspect {

    private final Cache<Long, NewsDtoResponse> newsCache;

    @Pointcut("@annotation(ru.clevertec.nms.annotation.CacheAlong)")
    private void annotatedMethod() {
    }

    @Pointcut("within(ru.clevertec.nms.service.NewsService+)")
    private void newsServicePointCut() {
    }

    /**
     * Advice processing the get News operation. If News is in cache it will get it from cache.
     * If News isn't in cache it will proceed initial operation and add data to cache.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && newsServicePointCut() && execution(* findById(..))")
    public Object cacheGetById(ProceedingJoinPoint joinPoint) throws Throwable {
        return CacheUtils.processCacheGetById(joinPoint, newsCache);
    }

    /**
     * Advice processing save and update News operations. Update News and add it to cache
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && newsServicePointCut() && (execution(* save(..)) || execution(* update*(..)))")
    public Object cachePost(ProceedingJoinPoint joinPoint) throws Throwable {
        NewsDtoResponse proceeded = (NewsDtoResponse) joinPoint.proceed(joinPoint.getArgs());
        newsCache.put(proceeded.getId(), proceeded);

        return proceeded;
    }

    /**
     * Advice processing delete News operation. Deletes News and deletes it from cache
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && newsServicePointCut() && execution(* deleteById(..))")
    public Object cacheDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheUtils.processCacheDelete(joinPoint, newsCache);
        return null;
    }
}
