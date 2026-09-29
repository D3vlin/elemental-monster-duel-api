package co.d3vlin.elementalmonsterduel.api.whatsnew.translation.repository;

import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryTranslationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhatsNewEntryTranslationRepository
        extends JpaRepository<WhatsNewEntryTranslationEntity, WhatsNewEntryTranslationId> {
    List<WhatsNewEntryTranslationEntity> findByIdLocaleId(Long localeId);
}
