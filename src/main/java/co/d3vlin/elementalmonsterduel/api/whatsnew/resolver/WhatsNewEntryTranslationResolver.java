package co.d3vlin.elementalmonsterduel.api.whatsnew.resolver;

import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.api.whatsnew.translation.repository.WhatsNewEntryTranslationRepository;
import co.d3vlin.elementalmonsterduel.dto.WhatsNewEntryDTO;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryTranslationEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WhatsNewEntryTranslationResolver {

    private final LanguageResolver languageResolver;
    private final WhatsNewEntryTranslationRepository whatsNewEntryTranslationRepository;

    @Transactional(readOnly = true)
    public List<WhatsNewEntryDTO> resolveTranslations(List<WhatsNewEntryDTO> entries, String lang) {
        LocaleEntity locale = languageResolver.resolve(lang);

        Map<Long, WhatsNewEntryTranslationEntity> translationByEntryId = whatsNewEntryTranslationRepository
                .findByIdLocaleId(locale.getId())
                .stream()
                .collect(Collectors.toMap(t -> t.getId().getWhatsNewEntryId(), t -> t));

        return entries.stream()
                .filter(entry -> translationByEntryId.containsKey(entry.getId()))
                .peek(entry -> {
                    WhatsNewEntryTranslationEntity translation = translationByEntryId.get(entry.getId());
                    entry.setTitle(translation.getTitle());
                    entry.setBody(translation.getBody());
                })
                .collect(Collectors.toList());
    }
}
