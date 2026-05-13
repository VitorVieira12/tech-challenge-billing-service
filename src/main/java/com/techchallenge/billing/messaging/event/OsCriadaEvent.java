package com.techchallenge.billing.messaging.event;

import java.time.LocalDateTime;

public record OsCriadaEvent(
    Long osId,
    String clienteNome,
    String veiculoPlaca,
    String descricaoProblema,
    LocalDateTime criadoEm
) {}
