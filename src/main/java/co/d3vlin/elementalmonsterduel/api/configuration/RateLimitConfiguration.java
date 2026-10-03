package co.d3vlin.elementalmonsterduel.api.configuration;

import co.d3vlin.elementalmonsterduel.api.security.ClientIpResolver;
import co.d3vlin.elementalmonsterduel.api.security.RateLimitFilter;
import co.d3vlin.elementalmonsterduel.api.security.RateLimiter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter(
            RateLimiter rateLimiter, ClientIpResolver clientIpResolver) {
        FilterRegistrationBean<RateLimitFilter> registration =
                new FilterRegistrationBean<>(new RateLimitFilter(rateLimiter, clientIpResolver));
        registration.setName("rateLimitFilter");
        registration.addUrlPatterns("/cards", "/cards/*", "/whats-new", "/whats-new/*");
        return registration;
    }
}
