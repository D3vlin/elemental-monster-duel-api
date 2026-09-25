package co.d3vlin.elementalmonsterduel.api.card.repository;

import co.d3vlin.elementalmonsterduel.entity.ElementTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.ElementTranslationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementTranslationRepository extends JpaRepository<ElementTranslationEntity, ElementTranslationId> {
    List<ElementTranslationEntity> findByIdLocaleId(Long localeId);
}
