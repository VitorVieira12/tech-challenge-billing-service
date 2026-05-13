package com.techchallenge.billing.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrcamentoGeradoEvent(
    Long orcamentoId,
    Long osId,
    BigDecimal valor,
    String descricao,
    LocalDateTime geradoEm
) {}
