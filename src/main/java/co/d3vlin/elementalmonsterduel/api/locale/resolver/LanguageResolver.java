package co.d3vlin.elementalmonsterduel.api.locale.resolver;

import co.d3vlin.elementalmonsterduel.api.exception.UnsupportedLanguageException;
import co.d3vlin.elementalmonsterduel.api.locale.repository.LocaleRepository;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LanguageResolver {

    private final LocaleRepository localeRepository;

    @Transactional(readOnly = true)
    public LocaleEntity resolve(String lang) {
        return localeRepository
                .findByCode(lang)
                .orElseThrow(() -> new UnsupportedLanguageException(lang));
    }
}
