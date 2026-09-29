package co.d3vlin.elementalmonsterduel.api.whatsnew.repository;

import co.d3vlin.elementalmonsterduel.entity.WhatsNewEntryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhatsNewEntryRepository extends JpaRepository<WhatsNewEntryEntity, Long> {
    List<WhatsNewEntryEntity> findAllByOrderByPublishedAtDescIdDesc();
}
