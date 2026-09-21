package co.d3vlin.elementalmonsterduel.api.duelresultcounter.controller;

import co.d3vlin.elementalmonsterduel.api.duelresultcounter.service.DuelResultCounterService;
import co.d3vlin.elementalmonsterduel.dto.DuelResultCounterDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/duel-result-counter")
@RequiredArgsConstructor
@Tag(name = "Duel Result Counter Controller", description = "Controller for reading the duel result counter")
public class DuelResultCounterController {
    private final DuelResultCounterService duelResultCounterService;

    @GetMapping
    public ResponseEntity<DuelResultCounterDTO> find() {
        return duelResultCounterService
                .find()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
