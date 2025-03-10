package edu.oregonstate.cs467.travelplanner.experience.web.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ConcurrentLruCache;
import org.sqids.Sqids;

import java.util.List;

@Component
public class ExperienceHashIdEncoder {
    private final Sqids sqids;
    private final ConcurrentLruCache<Long, String> encoderCache;
    private final ConcurrentLruCache<String, Long> decoderCache;

    public ExperienceHashIdEncoder(
            @Value("${sqids.alphabet}")
            String alphabet,
            @Value("8")
            int minLength,
            @Value("1000")
            int cacheCapacity
    ) {
        sqids = Sqids.builder().alphabet(alphabet).minLength(minLength).build();
        encoderCache = new ConcurrentLruCache<>(cacheCapacity, id -> sqids.encode(List.of(id)));
        decoderCache = new ConcurrentLruCache<>(cacheCapacity, hashId -> sqids.decode(hashId).getFirst());
    }

    public String encode(Long experienceId) {
        return encoderCache.get(experienceId);
    }

    public Long decode(String hashId) {
        return decoderCache.get(hashId);
    }
}
