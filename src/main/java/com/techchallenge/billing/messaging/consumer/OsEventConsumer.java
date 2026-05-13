package com.techchallenge.billing.messaging.consumer;

import com.techchallenge.billing.config.RabbitMQConfig;
import com.techchallenge.billing.domain.service.OrcamentoService;
import com.techchallenge.billing.messaging.event.OsCriadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OsEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OsEventConsumer.class);
    private final OrcamentoService orcamentoService;

    public OsEventConsumer(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_BILLING_OS_CRIADA)
    public void consumeOsCriada(OsCriadaEvent event) {
        log.info("Evento os.criada recebido: osId={}", event.osId());
        orcamentoService.gerarOrcamento(event);
    }
}
