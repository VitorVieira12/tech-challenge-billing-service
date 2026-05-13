package com.techchallenge.billing.domain.dto;

import com.techchallenge.billing.domain.model.Pagamento;
import com.techchallenge.billing.domain.model.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
    Long id,
    Long orcamentoId,
    Long osId,
    BigDecimal valor,
    StatusPagamento status,
    String linkPagamento,
    LocalDateTime criadoEm,
    LocalDateTime confirmadoEm
) {
    public static PagamentoResponseDTO from(Pagamento p) {
        return new PagamentoResponseDTO(
            p.getId(), p.getOrcamentoId(), p.getOsId(), p.getValor(),
            p.getStatus(), p.getLinkPagamento(), p.getCriadoEm(), p.getConfirmadoEm()
        );
    }
}
