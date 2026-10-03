package co.d3vlin.elementalmonsterduel.api.card.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.d3vlin.elementalmonsterduel.api.card.translation.repository.CardTranslationRepository;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.CardTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.CardTranslationId;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardLoreResolverTest {

    @Mock private CardTranslationRepository cardTranslationRepository;

    private CardLoreResolver cardLoreResolver;

    @BeforeEach
    void setUp() {
        cardLoreResolver = new CardLoreResolver(cardTranslationRepository);
    }

    private LocaleEntity locale(long id, String code) {
        LocaleEntity locale = new LocaleEntity();
        locale.setId(id);
        locale.setCode(code);
        return locale;
    }

    private CardTranslationEntity cardTranslation(long cardId, long localeId, String lore) {
        CardTranslationId id = new CardTranslationId();
        id.setCardId(cardId);
        id.setLocaleId(localeId);
        CardTranslationEntity entity = new CardTranslationEntity();
        entity.setId(id);
        entity.setLore(lore);
        return entity;
    }

    @Test
    void resolvesLoreForTheRequestedLocale() {
        LocaleEntity es = locale(1L, "es");
        when(cardTranslationRepository.findByIdLocaleId(1L))
                .thenReturn(List.of(cardTranslation(42L, 1L, "Una historia en español.")));

        CardDTO card = new CardDTO();
        card.setId(42L);

        cardLoreResolver.resolveLores(List.of(card), es);

        assertThat(card.getLore()).isEqualTo("Una historia en español.");
    }

    @Test
    void leavesLoreNullWhenNoTranslationExistsForTheCard() {
        LocaleEntity en = locale(2L, "en");
        when(cardTranslationRepository.findByIdLocaleId(2L)).thenReturn(List.of());

        CardDTO card = new CardDTO();
        card.setId(42L);

        cardLoreResolver.resolveLores(List.of(card), en);

        assertThat(card.getLore()).isNull();
    }
}
