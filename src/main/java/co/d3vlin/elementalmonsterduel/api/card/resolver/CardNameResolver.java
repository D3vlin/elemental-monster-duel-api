package co.d3vlin.elementalmonsterduel.api.card.resolver;

import co.d3vlin.elementalmonsterduel.api.elementTranslation.repository.ElementTranslationRepository;
import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.api.powerRankTranslation.repository.PowerRankTranslationRepository;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.ElementTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.entity.PowerRankTranslationEntity;
import co.d3vlin.elementalmonsterduel.enums.Element;
import co.d3vlin.elementalmonsterduel.enums.PowerRank;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CardNameResolver {

    private static final Map<String, String> NAME_TEMPLATE_BY_LOCALE = Map.of(
            "es", "%s de %s",
            "en", "%s of %s");

    private final LanguageResolver languageResolver;
    private final ElementTranslationRepository elementTranslationRepository;
    private final PowerRankTranslationRepository powerRankTranslationRepository;

    @Transactional(readOnly = true)
    public void resolveNames(Collection<CardDTO> cards, String lang) {
        LocaleEntity locale = languageResolver.resolve(lang);

        Map<Element, String> elementLabels = elementTranslationRepository
                .findByIdLocaleId(locale.getId())
                .stream()
                .collect(Collectors.toMap(t -> t.getId().getElement(), ElementTranslationEntity::getLabel));

        Map<PowerRank, String> powerRankLabels = powerRankTranslationRepository
                .findByIdLocaleId(locale.getId())
                .stream()
                .collect(Collectors.toMap(t -> t.getId().getPowerRank(), PowerRankTranslationEntity::getLabel));

        String nameTemplate = NAME_TEMPLATE_BY_LOCALE.getOrDefault(lang, NAME_TEMPLATE_BY_LOCALE.get("es"));

        for (CardDTO card : cards) {
            card.setName(String.format(
                    nameTemplate,
                    powerRankLabels.get(card.getPowerRank()),
                    elementLabels.get(card.getElement())));
        }
    }
}
