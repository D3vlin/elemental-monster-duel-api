package co.d3vlin.elementalmonsterduel.api.locale.repository;

import co.d3vlin.elementalmonsterduel.entity.LocaleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocaleRepository extends JpaRepository<LocaleEntity, Long> {
    Optional<LocaleEntity> findByCode(String code);
}
