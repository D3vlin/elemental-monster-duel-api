package co.d3vlin.elementalmonsterduel.api.cardTranslation.repository;

import co.d3vlin.elementalmonsterduel.entity.CardTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.CardTranslationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTranslationRepository extends JpaRepository<CardTranslationEntity, CardTranslationId> {
    List<CardTranslationEntity> findByIdLocaleId(Long localeId);
}
