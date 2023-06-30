package ru.clevertec.nms.util;

import org.aspectj.lang.ProceedingJoinPoint;
import ru.clevertec.nms.cache.Cache;

public class CacheUtils {

    public static <K, V> V processCacheGetById(ProceedingJoinPoint joinPoint, Cache<K, V> cache) throws Throwable {
        V result;
        Object[] args = joinPoint.getArgs();

        result = cache.get((K) args[0]);
        if (result == null) {
            result = (V) joinPoint.proceed(args);
            cache.put((K) args[0], (V) result);
        }

        return result;
    }

    public static <K, V> void processCacheDelete(ProceedingJoinPoint joinPoint, Cache<K, V> cache) throws Throwable {
        Object[] args = joinPoint.getArgs();

        joinPoint.proceed(args);
        cache.delete((K) args[0]);
    }
}
