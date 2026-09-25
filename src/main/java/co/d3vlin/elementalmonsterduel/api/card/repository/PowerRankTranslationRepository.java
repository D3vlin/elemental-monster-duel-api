package co.d3vlin.elementalmonsterduel.api.card.repository;

import co.d3vlin.elementalmonsterduel.entity.PowerRankTranslationEntity;
import co.d3vlin.elementalmonsterduel.entity.PowerRankTranslationId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerRankTranslationRepository extends JpaRepository<PowerRankTranslationEntity, PowerRankTranslationId> {
    List<PowerRankTranslationEntity> findByIdLocaleId(Long localeId);
}
