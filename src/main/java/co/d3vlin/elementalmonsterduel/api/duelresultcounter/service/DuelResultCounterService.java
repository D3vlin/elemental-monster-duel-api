package co.d3vlin.elementalmonsterduel.api.duelresultcounter.service;

import co.d3vlin.elementalmonsterduel.api.duelresultcounter.repository.DuelResultCounterRepository;
import co.d3vlin.elementalmonsterduel.dto.DuelResultCounterDTO;
import co.d3vlin.elementalmonsterduel.mapper.DuelResultCounterMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuelResultCounterService {
    private final DuelResultCounterRepository duelResultCounterRepository;
    private final DuelResultCounterMapper duelResultCounterMapper;

    @Transactional(readOnly = true)
    public Optional<DuelResultCounterDTO> find() {
        return duelResultCounterRepository
                .findAll()
                .stream()
                .findFirst()
                .map(duelResultCounterMapper::fromEntity);
    }
}
