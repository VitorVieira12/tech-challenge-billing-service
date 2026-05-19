package com.techchallenge.billing.domain.dto;

import com.techchallenge.billing.domain.model.Orcamento;
import com.techchallenge.billing.domain.model.StatusOrcamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoResponseDTO(
    Long id,
    Long osId,
    BigDecimal valor,
    String descricao,
    StatusOrcamento status,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public static OrcamentoResponseDTO from(Orcamento o) {
        return new OrcamentoResponseDTO(
            o.getId(), o.getOsId(), o.getValor(), o.getDescricao(),
            o.getStatus(), o.getCriadoEm(), o.getAtualizadoEm()
        );
    }
}
