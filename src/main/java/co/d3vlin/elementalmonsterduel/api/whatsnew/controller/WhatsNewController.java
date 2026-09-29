package co.d3vlin.elementalmonsterduel.api.whatsnew.controller;

import co.d3vlin.elementalmonsterduel.api.whatsnew.service.WhatsNewService;
import co.d3vlin.elementalmonsterduel.dto.WhatsNewEntryDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/whats-new")
@RequiredArgsConstructor
@Tag(name = "What's New Controller", description = "Controller for reading player-facing What's New entries")
public class WhatsNewController {
    private final WhatsNewService whatsNewService;

    @GetMapping
    public ResponseEntity<List<WhatsNewEntryDTO>> findAll(
            @Parameter(description = "Language for the entry text (es|en)") @RequestParam(defaultValue = "es") String lang) {
        return ResponseEntity.ok(whatsNewService.findAll(lang));
    }
}
