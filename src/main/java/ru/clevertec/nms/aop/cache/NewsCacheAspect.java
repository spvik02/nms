package ru.clevertec.nms.aop.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.clevertec.nms.cache.Cache;
import ru.clevertec.nms.model.dto.NewsDtoResponse;
import ru.clevertec.nms.util.CacheUtils;

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

    @Around("annotatedMethod() && newsServicePointCut() && execution(* findById(..))")
    public Object cacheGetById(ProceedingJoinPoint joinPoint) throws Throwable {
        return CacheUtils.processCacheGetById(joinPoint, newsCache);
    }

    @Around("annotatedMethod() && newsServicePointCut() && (execution(* save(..)) || execution(* update*(..)))")
    public Object cachePost(ProceedingJoinPoint joinPoint) throws Throwable {
        NewsDtoResponse proceeded = (NewsDtoResponse) joinPoint.proceed(joinPoint.getArgs());
        newsCache.put(proceeded.getId(), proceeded);

        return proceeded;
    }

    @Around("annotatedMethod() && newsServicePointCut() && execution(* deleteById(..))")
    public Object cacheDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        CacheUtils.processCacheDelete(joinPoint, newsCache);
        return null;
    }
}
