package co.d3vlin.elementalmonsterduel.api.whatsnew.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.d3vlin.elementalmonsterduel.api.whatsnew.repository.WhatsNewEntryRepository;
import co.d3vlin.elementalmonsterduel.api.whatsnew.resolver.WhatsNewEntryTranslationResolver;
import co.d3vlin.elementalmonsterduel.dto.WhatsNewEntryDTO;
import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryEntity;
import co.d3vlin.elementalmonsterduel.mapper.WhatsNewEntryMapper;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WhatsNewServiceTest {

    @Mock
    private WhatsNewEntryRepository whatsNewEntryRepository;

    @Mock
    private WhatsNewEntryMapper whatsNewEntryMapper;

    @Mock
    private WhatsNewEntryTranslationResolver whatsNewEntryTranslationResolver;

    @InjectMocks
    private WhatsNewService whatsNewService;

    @Test
    void mapsRepositoryResultsAndResolvesTranslationsForTheRequestedLang() {
        WhatsNewEntryEntity entity = new WhatsNewEntryEntity();
        entity.setId(1L);
        entity.setSlug("launch");
        entity.setPublishedAt(Instant.parse("2026-09-25T00:00:00Z"));

        WhatsNewEntryDTO mapped = new WhatsNewEntryDTO();
        mapped.setId(1L);

        WhatsNewEntryDTO resolved = new WhatsNewEntryDTO();
        resolved.setId(1L);
        resolved.setTitle("¡Bienvenido!");

        when(whatsNewEntryRepository.findAllByOrderByPublishedAtDescIdDesc()).thenReturn(List.of(entity));
        when(whatsNewEntryMapper.fromEntity(entity)).thenReturn(mapped);
        when(whatsNewEntryTranslationResolver.resolveTranslations(List.of(mapped), "es")).thenReturn(List.of(resolved));

        List<WhatsNewEntryDTO> result = whatsNewService.findAll("es");

        assertThat(result).containsExactly(resolved);
    }
}
