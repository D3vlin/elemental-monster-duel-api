package co.d3vlin.elementalmonsterduel.api.card.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import co.d3vlin.elementalmonsterduel.api.element.translation.repository.ElementTranslationRepository;
import co.d3vlin.elementalmonsterduel.api.powerrank.translation.repository.PowerRankTranslationRepository;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.ElementTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.ElementTranslationId;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.entity.PowerRankTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.PowerRankTranslationId;
import co.d3vlin.elementalmonsterduel.enums.Element;
import co.d3vlin.elementalmonsterduel.enums.PowerRank;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardNameResolverTest {

    @Mock private ElementTranslationRepository elementTranslationRepository;
    @Mock private PowerRankTranslationRepository powerRankTranslationRepository;

    private CardNameResolver cardNameResolver;

    @BeforeEach
    void setUp() {
        cardNameResolver = new CardNameResolver(elementTranslationRepository, powerRankTranslationRepository);
    }

    private LocaleEntity locale(long id, String code) {
        LocaleEntity locale = new LocaleEntity();
        locale.setId(id);
        locale.setCode(code);
        return locale;
    }

    private ElementTranslationEntity elementTranslation(Element element, long localeId, String label) {
        ElementTranslationId id = new ElementTranslationId();
        id.setElement(element);
        id.setLocaleId(localeId);
        ElementTranslationEntity entity = new ElementTranslationEntity();
        entity.setId(id);
        entity.setLabel(label);
        return entity;
    }

    private PowerRankTranslationEntity powerRankTranslation(PowerRank powerRank, long localeId, String label) {
        PowerRankTranslationId id = new PowerRankTranslationId();
        id.setPowerRank(powerRank);
        id.setLocaleId(localeId);
        PowerRankTranslationEntity entity = new PowerRankTranslationEntity();
        entity.setId(id);
        entity.setLabel(label);
        return entity;
    }

    @Test
    void composesNameUsingTheSpanishTemplateForEsLocale() {
        LocaleEntity es = locale(1L, "es");
        when(elementTranslationRepository.findByIdLocaleId(1L))
                .thenReturn(List.of(elementTranslation(Element.FIRE, 1L, "Fuego")));
        when(powerRankTranslationRepository.findByIdLocaleId(1L))
                .thenReturn(List.of(powerRankTranslation(PowerRank.SPAWN, 1L, "Engendro")));

        CardDTO card = new CardDTO();
        card.setElement(Element.FIRE);
        card.setPowerRank(PowerRank.SPAWN);

        cardNameResolver.resolveNames(List.of(card), es);

        assertThat(card.getName()).isEqualTo("Engendro de Fuego");
    }

    @Test
    void composesNameUsingTheEnglishTemplateForEnLocaleRegardlessOfAnyRawInput() {
        LocaleEntity en = locale(2L, "en");
        when(elementTranslationRepository.findByIdLocaleId(2L))
                .thenReturn(List.of(elementTranslation(Element.FIRE, 2L, "Fire")));
        when(powerRankTranslationRepository.findByIdLocaleId(2L))
                .thenReturn(List.of(powerRankTranslation(PowerRank.SPAWN, 2L, "Spawn")));

        CardDTO card = new CardDTO();
        card.setElement(Element.FIRE);
        card.setPowerRank(PowerRank.SPAWN);

        cardNameResolver.resolveNames(List.of(card), en);

        assertThat(card.getName()).isEqualTo("Spawn of Fire");
    }
}
