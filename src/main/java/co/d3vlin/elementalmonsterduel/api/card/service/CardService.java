package co.d3vlin.elementalmonsterduel.api.card.service;

import co.d3vlin.elementalmonsterduel.api.card.repository.CardRepository;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import co.d3vlin.elementalmonsterduel.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional(readOnly = true)
    public Page<CardDTO> findAll(Pageable pageable) {
        return cardRepository
                .findAll(pageable)
                .map(cardMapper::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<CardDTO> findById(Long id) {
        return cardRepository
                .findById(id)
                .map(cardMapper::fromEntity);
    }
}
