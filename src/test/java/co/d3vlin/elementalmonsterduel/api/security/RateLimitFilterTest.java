package co.d3vlin.elementalmonsterduel.api.security;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {

    @Mock private RateLimiter rateLimiter;
    @Mock private ClientIpResolver clientIpResolver;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain chain;

    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RateLimitFilter(rateLimiter, clientIpResolver);
        lenient().when(request.getContextPath()).thenReturn("/elemental-monster-duel");
        lenient().when(request.getRequestURI()).thenReturn("/elemental-monster-duel/cards");
        lenient().when(clientIpResolver.resolve(request)).thenReturn("10.0.0.1");
    }

    @Test
    void allowsRequestThroughWhenUnderLimit() throws Exception {
        when(rateLimiter.allow("/cards:10.0.0.1")).thenReturn(true);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void rejectsRequestWithTooManyRequestsWhenOverLimit() throws Exception {
        when(rateLimiter.allow("/cards:10.0.0.1")).thenReturn(false);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        filter.doFilter(request, response, chain);

        verify(response).setStatus(429);
        verifyNoInteractions(chain);
    }
}
