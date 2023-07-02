package ru.clevertec.nms.aop.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.nms.cache.Cache;
import ru.clevertec.nms.model.dto.CommentDtoResponse;
import ru.clevertec.nms.util.CacheUtils;

/**
 * AOP for work with Comment cache
 */
@Profile("cache")
@Aspect
@Component
@RequiredArgsConstructor
public class CommentCacheAspect {

    private final Cache<Long, CommentDtoResponse> commentCache;

    @Pointcut("@annotation(ru.clevertec.nms.annotation.CacheAlong)")
    private void annotatedMethod() {
    }

    @Pointcut("within(ru.clevertec.nms.service.CommentService+)")
    private void commentServicePointCut() {
    }

    /**
     * Advice processing the get Comment operation. If comment is in cache it will get it from cache.
     * If comment isn't in cache it will proceed initial operation and add data to cache.
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && commentServicePointCut() && execution(* findById(..))")
    public Object cacheGetById(ProceedingJoinPoint joinPoint) throws Throwable {
        return CacheUtils.processCacheGetById(joinPoint, commentCache);
    }

    /**
     * Advice processing save and update Comment operations. Update comment and add it to cache
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && " +
            "commentServicePointCut() && " +
            "(execution(* save(..)) || execution(* update*(..)))")
    public Object cachePost(ProceedingJoinPoint joinPoint) throws Throwable {
        CommentDtoResponse proceeded = (CommentDtoResponse) joinPoint.proceed(joinPoint.getArgs());
        commentCache.put(proceeded.getId(), proceeded);

        return proceeded;
    }

    /**
     * Advice processing delete Comment operation. Deletes comment and deletes it from cache
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("annotatedMethod() && commentServicePointCut() && execution(* deleteById(..))")
    public Object cacheDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheUtils.processCacheDelete(joinPoint, commentCache);
        return null;
    }
}
