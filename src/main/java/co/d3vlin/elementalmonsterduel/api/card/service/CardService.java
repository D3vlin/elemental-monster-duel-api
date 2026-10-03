package co.d3vlin.elementalmonsterduel.api.card.service;

import co.d3vlin.elementalmonsterduel.api.card.repository.CardRepository;
import co.d3vlin.elementalmonsterduel.api.card.resolver.CardLoreResolver;
import co.d3vlin.elementalmonsterduel.api.card.resolver.CardNameResolver;
import co.d3vlin.elementalmonsterduel.api.locale.resolver.LanguageResolver;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import co.d3vlin.elementalmonsterduel.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardNameResolver cardNameResolver;
    private final CardLoreResolver cardLoreResolver;
    private final LanguageResolver languageResolver;

    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable, String lang) {
        LocaleEntity locale = languageResolver.resolve(lang);
        Page<CardDTO> cards = cardRepository
                .findAll(pageable)
                .map(cardMapper::fromEntity);
        cardNameResolver.resolveNames(cards.getContent(), locale);
        cardLoreResolver.resolveLores(cards.getContent(), locale);
        return cards;
    }

    @Transactional(readOnly = true)
    public Optional<CardDTO> findById(Long id, String lang) {
        LocaleEntity locale = languageResolver.resolve(lang);
        Optional<CardDTO> card = cardRepository
                .findById(id)
                .map(cardMapper::fromEntity);
        card.ifPresent(c -> {
            cardNameResolver.resolveNames(List.of(c), locale);
            cardLoreResolver.resolveLores(List.of(c), locale);
        });
        return card;
    }
}
