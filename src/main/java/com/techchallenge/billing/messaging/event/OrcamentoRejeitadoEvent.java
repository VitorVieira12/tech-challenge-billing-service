package com.techchallenge.billing.messaging.event;

import java.time.LocalDateTime;

public record OrcamentoRejeitadoEvent(
    Long orcamentoId,
    Long osId,
    String motivo,
    LocalDateTime rejeitadoEm
) {}
