package com.techchallenge.billing.domain.repository;

import com.techchallenge.billing.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByOrcamentoId(Long orcamentoId);
    List<Pagamento> findByOsId(Long osId);
}
