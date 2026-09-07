package co.d3vlin.elementalmonsterduel.api.duelresultcounter.repository;

import co.d3vlin.elementalmonsterduel.entity.DuelResultCounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuelResultCounterRepository extends JpaRepository<DuelResultCounterEntity, Integer> {
}
