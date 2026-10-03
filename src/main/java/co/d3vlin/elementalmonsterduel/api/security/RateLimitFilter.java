package co.d3vlin.elementalmonsterduel.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RateLimitFilter extends HttpFilter {

    private final RateLimiter rateLimiter;
    private final ClientIpResolver clientIpResolver;

    public RateLimitFilter(RateLimiter rateLimiter, ClientIpResolver clientIpResolver) {
        this.rateLimiter = rateLimiter;
        this.clientIpResolver = clientIpResolver;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String bucketKey = path + ":" + clientIpResolver.resolve(request);

        if (!rateLimiter.allow(bucketKey)) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Too many requests. Please try again later.\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
