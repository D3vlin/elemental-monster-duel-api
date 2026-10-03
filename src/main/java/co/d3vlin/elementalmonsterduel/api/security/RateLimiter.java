package co.d3vlin.elementalmonsterduel.api.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RateLimiter {

    private static final int MAX_REQUESTS_PER_WINDOW = 60;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final Clock clock;
    private final ConcurrentHashMap<String, Window> windowsByBucket = new ConcurrentHashMap<>();
    private final AtomicLong rejectionCount = new AtomicLong();

    public RateLimiter(Clock clock) {
        this.clock = clock;
    }

    public boolean allow(String bucketKey) {
        Window window = windowsByBucket.computeIfAbsent(bucketKey, key -> new Window(clock.instant()));

        synchronized (window) {
            if (clock.instant().isAfter(window.startedAt.plus(WINDOW))) {
                window.startedAt = clock.instant();
                window.requestCount = 0;
            }
            if (window.requestCount >= MAX_REQUESTS_PER_WINDOW) {
                rejectionCount.incrementAndGet();
                return false;
            }
            window.requestCount++;
            return true;
        }
    }

    public long rejectionCount() {
        return rejectionCount.get();
    }

    private static final class Window {
        private Instant startedAt;
        private int requestCount;

        private Window(Instant startedAt) {
            this.startedAt = startedAt;
        }
    }
}
