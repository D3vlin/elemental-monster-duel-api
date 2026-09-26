package co.d3vlin.elementalmonsterduel.api.card.service;

import co.d3vlin.elementalmonsterduel.api.card.repository.CardRepository;
import co.d3vlin.elementalmonsterduel.api.card.resolver.CardNameResolver;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
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

    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable, String lang) {
        Page<CardDTO> cards = cardRepository
                .findAll(pageable)
                .map(cardMapper::fromEntity);
        cardNameResolver.resolveNames(cards.getContent(), lang);
        return cards;
    }

    @Transactional(readOnly = true)
    public Optional<CardDTO> findById(Long id, String lang) {
        Optional<CardDTO> card = cardRepository
                .findById(id)
                .map(cardMapper::fromEntity);
        card.ifPresent(c -> cardNameResolver.resolveNames(List.of(c), lang));
        return card;
    }
}
