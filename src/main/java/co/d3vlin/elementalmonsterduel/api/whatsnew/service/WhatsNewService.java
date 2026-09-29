package co.d3vlin.elementalmonsterduel.api.whatsnew.service;

import co.d3vlin.elementalmonsterduel.api.whatsnew.repository.WhatsNewEntryRepository;
import co.d3vlin.elementalmonsterduel.api.whatsnew.resolver.WhatsNewEntryTranslationResolver;
import co.d3vlin.elementalmonsterduel.dto.WhatsNewEntryDTO;
import co.d3vlin.elementalmonsterduel.mapper.WhatsNewEntryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WhatsNewService {
    private final WhatsNewEntryRepository whatsNewEntryRepository;
    private final WhatsNewEntryMapper whatsNewEntryMapper;
    private final WhatsNewEntryTranslationResolver whatsNewEntryTranslationResolver;

    @Transactional(readOnly = true)
    public List<WhatsNewEntryDTO> findAll(String lang) {
        List<WhatsNewEntryDTO> entries = whatsNewEntryRepository.findAllByOrderByPublishedAtDescIdDesc().stream()
                .map(whatsNewEntryMapper::fromEntity)
                .toList();
        return whatsNewEntryTranslationResolver.resolveTranslations(entries, lang);
    }
}
