package co.d3vlin.elementalmonsterduel.api.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientIpResolverTest {

    private final ClientIpResolver resolver = new ClientIpResolver();

    @Mock
    private HttpServletRequest request;

    @Test
    void resolvesFromRemoteAddrWhenNoForwardedForHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        assertThat(resolver.resolve(request)).isEqualTo("10.0.0.1");
    }

    @Test
    void resolvesFromFirstEntryOfForwardedForHeader() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("203.0.113.5, 10.0.0.1");

        assertThat(resolver.resolve(request)).isEqualTo("203.0.113.5");
    }

    @Test
    void fallsBackToRemoteAddrWhenForwardedForIsBlank() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("   ");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        assertThat(resolver.resolve(request)).isEqualTo("10.0.0.1");
    }
}
