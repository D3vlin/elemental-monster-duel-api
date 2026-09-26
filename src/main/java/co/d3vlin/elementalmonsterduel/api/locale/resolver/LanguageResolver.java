package co.d3vlin.elementalmonsterduel.api.locale.resolver;

import co.d3vlin.elementalmonsterduel.api.locale.repository.LocaleRepository;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class LanguageResolver {

    private final LocaleRepository localeRepository;

    @Transactional(readOnly = true)
    public LocaleEntity resolve(String lang) {
        return localeRepository
                .findByCode(lang)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported lang: " + lang));
    }
}
