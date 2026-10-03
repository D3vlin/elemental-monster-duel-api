package co.d3vlin.elementalmonsterduel.api.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class RateLimiterTest {

    @Test
    void allowsRequestsUpToTheLimit() {
        MutableClock clock = new MutableClock(Instant.parse("2026-10-03T00:00:00Z"));
        RateLimiter rateLimiter = new RateLimiter(clock);

        for (int i = 0; i < 60; i++) {
            assertThat(rateLimiter.allow("/cards:1.2.3.4")).isTrue();
        }
    }

    @Test
    void rejectsRequestsOverTheLimitWithinTheSameWindow() {
        MutableClock clock = new MutableClock(Instant.parse("2026-10-03T00:00:00Z"));
        RateLimiter rateLimiter = new RateLimiter(clock);
        for (int i = 0; i < 60; i++) {
            rateLimiter.allow("/cards:1.2.3.4");
        }

        assertThat(rateLimiter.allow("/cards:1.2.3.4")).isFalse();
        assertThat(rateLimiter.rejectionCount()).isEqualTo(1);
    }

    @Test
    void resetsTheCountOnceTheWindowElapses() {
        MutableClock clock = new MutableClock(Instant.parse("2026-10-03T00:00:00Z"));
        RateLimiter rateLimiter = new RateLimiter(clock);
        for (int i = 0; i < 60; i++) {
            rateLimiter.allow("/cards:1.2.3.4");
        }
        assertThat(rateLimiter.allow("/cards:1.2.3.4")).isFalse();

        clock.advance(Duration.ofMinutes(1).plusSeconds(1));

        assertThat(rateLimiter.allow("/cards:1.2.3.4")).isTrue();
    }

    @Test
    void tracksEachBucketIndependently() {
        MutableClock clock = new MutableClock(Instant.parse("2026-10-03T00:00:00Z"));
        RateLimiter rateLimiter = new RateLimiter(clock);
        for (int i = 0; i < 60; i++) {
            rateLimiter.allow("/cards:1.2.3.4");
        }

        assertThat(rateLimiter.allow("/whats-new:1.2.3.4")).isTrue();
        assertThat(rateLimiter.allow("/cards:5.6.7.8")).isTrue();
    }

    private static final class MutableClock extends Clock {
        private Instant instant;

        private MutableClock(Instant instant) {
            this.instant = instant;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
