package co.d3vlin.elementalmonsterduel.api.card.repository;

import co.d3vlin.elementalmonsterduel.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
}
