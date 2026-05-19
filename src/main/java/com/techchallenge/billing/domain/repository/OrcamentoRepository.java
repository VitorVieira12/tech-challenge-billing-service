package com.techchallenge.billing.domain.repository;

import com.techchallenge.billing.domain.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    Optional<Orcamento> findByOsId(Long osId);
}
