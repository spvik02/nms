package ru.clevertec.nms.util;

import org.aspectj.lang.ProceedingJoinPoint;
import ru.clevertec.nms.cache.Cache;

/**
 * Util class for processing common cache operation
 */
public class CacheUtils {

    /**
     * Performs processing of the get data operation. If data is in cache it will get it from cache.
     * If data isn't in cache it will proceed initial operation and add data to cache.
     *
     * @param joinPoint
     * @param cache
     * @param <K>       key
     * @param <V>       value
     * @return
     * @throws Throwable
     */
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

    /**
     * Performs processing of the delete data operation. If data is in cache it will proceed initial delete operation
     * and delete it from cache.
     *
     * @param joinPoint
     * @param cache
     * @param <K>       key
     * @param <V>       value
     * @throws Throwable
     */
    public static <K, V> void processCacheDelete(ProceedingJoinPoint joinPoint, Cache<K, V> cache) throws Throwable {
        Object[] args = joinPoint.getArgs();

        joinPoint.proceed(args);
        cache.delete((K) args[0]);
    }
}
