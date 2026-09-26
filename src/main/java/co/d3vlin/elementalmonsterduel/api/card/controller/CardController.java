package co.d3vlin.elementalmonsterduel.api.card.controller;

import co.d3vlin.elementalmonsterduel.api.card.service.CardService;
import co.d3vlin.elementalmonsterduel.dto.CardDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cards")
@RequiredArgsConstructor
@Tag(name = "Card Controller", description = "Controller for reading cards")
public class CardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Page<CardDTO>> findAll(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @Parameter(description = "Language for the card name (es|en)") @RequestParam(defaultValue = "es") String lang) {
        return ResponseEntity.ok(cardService.findAll(pageable, lang));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CardDTO> findById(
            @PathVariable Long id,
            @Parameter(description = "Language for the card name (es|en)") @RequestParam(defaultValue = "es") String lang) {
        return cardService
                .findById(id, lang)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
