package co.d3vlin.elementalmonsterduel.api.card.resolver;

import co.d3vlin.elementalmonsterduel.api.cardTranslation.repository.CardTranslationRepository;
import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.CardTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CardLoreResolver {

    private final LanguageResolver languageResolver;
    private final CardTranslationRepository cardTranslationRepository;

    @Transactional(readOnly = true)
    public void resolveLores(Collection<CardDTO> cards, String lang) {
        LocaleEntity locale = languageResolver.resolve(lang);

        Map<Long, String> loreByCardId = cardTranslationRepository
                .findByIdLocaleId(locale.getId())
                .stream()
                .collect(Collectors.toMap(t -> t.getId().getCardId(), CardTranslationEntity::getLore));

        for (CardDTO card : cards) {
            card.setLore(loreByCardId.get(card.getId()));
        }
    }
}
