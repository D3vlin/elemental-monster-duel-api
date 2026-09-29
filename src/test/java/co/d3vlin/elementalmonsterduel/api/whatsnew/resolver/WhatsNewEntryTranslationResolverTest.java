package co.d3vlin.elementalmonsterduel.api.whatsnew.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.api.whatsnew.translation.repository.WhatsNewEntryTranslationRepository;
import co.d3vlin.elementalmonsterduel.dto.WhatsNewEntryDTO;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryTranslationId;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WhatsNewEntryTranslationResolverTest {

    @Mock
    private LanguageResolver languageResolver;

    @Mock
    private WhatsNewEntryTranslationRepository whatsNewEntryTranslationRepository;

    @InjectMocks
    private WhatsNewEntryTranslationResolver resolver;

    private LocaleEntity locale(long id, String code) {
        LocaleEntity locale = new LocaleEntity();
        locale.setId(id);
        locale.setCode(code);
        return locale;
    }

    private WhatsNewEntryTranslationEntity translation(Long entryId, Long localeId, String title, String body) {
        WhatsNewEntryTranslationId id = new WhatsNewEntryTranslationId();
        id.setWhatsNewEntryId(entryId);
        id.setLocaleId(localeId);

        WhatsNewEntryTranslationEntity entity = new WhatsNewEntryTranslationEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setBody(body);
        return entity;
    }

    private WhatsNewEntryDTO entry(Long id) {
        WhatsNewEntryDTO dto = new WhatsNewEntryDTO();
        dto.setId(id);
        return dto;
    }

    @Test
    void fillsTitleAndBodyForTheRequestedLocale() {
        when(languageResolver.resolve("es")).thenReturn(locale(1L, "es"));
        when(whatsNewEntryTranslationRepository.findByIdLocaleId(1L))
                .thenReturn(List.of(translation(10L, 1L, "Título", "Cuerpo")));

        List<WhatsNewEntryDTO> result = resolver.resolveTranslations(List.of(entry(10L)), "es");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Título");
        assertThat(result.get(0).getBody()).isEqualTo("Cuerpo");
    }

    @Test
    void dropsEntriesWithoutATranslationForTheRequestedLocale() {
        when(languageResolver.resolve("en")).thenReturn(locale(2L, "en"));
        when(whatsNewEntryTranslationRepository.findByIdLocaleId(2L))
                .thenReturn(List.of(translation(10L, 2L, "Title", "Body")));

        List<WhatsNewEntryDTO> result = resolver.resolveTranslations(List.of(entry(10L), entry(11L)), "en");

        assertThat(result).extracting(WhatsNewEntryDTO::getId).containsExactly(10L);
    }
}
