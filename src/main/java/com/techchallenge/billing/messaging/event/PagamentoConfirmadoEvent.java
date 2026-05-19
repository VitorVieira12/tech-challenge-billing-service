package com.techchallenge.billing.messaging.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoConfirmadoEvent(
    Long pagamentoId,
    Long orcamentoId,
    Long osId,
    BigDecimal valor,
    LocalDateTime confirmadoEm
) {}
