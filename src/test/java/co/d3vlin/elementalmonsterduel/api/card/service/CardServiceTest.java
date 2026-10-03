package co.d3vlin.elementalmonsterduel.api.card.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import co.d3vlin.elementalmonsterduel.api.card.repository.CardRepository;
import co.d3vlin.elementalmonsterduel.api.card.resolver.CardLoreResolver;
import co.d3vlin.elementalmonsterduel.api.card.resolver.CardNameResolver;
import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.CardEntity;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.mapper.CardMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardMapper cardMapper;
    @Mock private CardNameResolver cardNameResolver;
    @Mock private CardLoreResolver cardLoreResolver;
    @Mock private LanguageResolver languageResolver;

    private CardService cardService;
    private LocaleEntity locale;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, cardMapper, cardNameResolver, cardLoreResolver, languageResolver);
        locale = new LocaleEntity();
        locale.setId(1L);
        locale.setCode("es");
    }

    @Test
    void findAllResolvesLocaleOnlyOnceAndPassesItToBothResolvers() {
        CardEntity entity = new CardEntity();
        CardDTO dto = new CardDTO();
        Pageable pageable = Pageable.unpaged();
        when(languageResolver.resolve("es")).thenReturn(locale);
        when(cardRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity)));
        when(cardMapper.fromEntity(entity)).thenReturn(dto);

        Page<CardDTO> result = cardService.findAll(pageable, "es");

        assertThat(result.getContent()).containsExactly(dto);
        verify(languageResolver, times(1)).resolve("es");
        verify(cardNameResolver).resolveNames(List.of(dto), locale);
        verify(cardLoreResolver).resolveLores(List.of(dto), locale);
    }

    @Test
    void findByIdResolvesLocaleOnceAndPassesItToBothResolversWhenCardExists() {
        CardEntity entity = new CardEntity();
        CardDTO dto = new CardDTO();
        when(languageResolver.resolve("en")).thenReturn(locale);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(cardMapper.fromEntity(entity)).thenReturn(dto);

        Optional<CardDTO> result = cardService.findById(1L, "en");

        assertThat(result).contains(dto);
        verify(languageResolver, times(1)).resolve("en");
        verify(cardNameResolver).resolveNames(List.of(dto), locale);
        verify(cardLoreResolver).resolveLores(List.of(dto), locale);
    }

    @Test
    void findByIdStillResolvesLocaleButSkipsResolversWhenCardIsAbsent() {
        when(languageResolver.resolve("es")).thenReturn(locale);
        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CardDTO> result = cardService.findById(99L, "es");

        assertThat(result).isEmpty();
        verify(languageResolver, times(1)).resolve("es");
        verifyNoInteractions(cardNameResolver, cardLoreResolver);
    }
}
