package com.techchallenge.billing.messaging.publisher;

import com.techchallenge.billing.config.RabbitMQConfig;
import com.techchallenge.billing.messaging.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BillingEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BillingEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public BillingEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrcamentoGerado(OrcamentoGeradoEvent event) {
        log.info("Publicando orcamento.gerado para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BILLING_EVENTS_EXCHANGE, RabbitMQConfig.RK_ORCAMENTO_GERADO, event);
    }

    public void publishOrcamentoAprovado(OrcamentoAprovadoEvent event) {
        log.info("Publicando orcamento.aprovado para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BILLING_EVENTS_EXCHANGE, RabbitMQConfig.RK_ORCAMENTO_APROVADO, event);
    }

    public void publishOrcamentoRejeitado(OrcamentoRejeitadoEvent event) {
        log.info("Publicando orcamento.rejeitado para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BILLING_EVENTS_EXCHANGE, RabbitMQConfig.RK_ORCAMENTO_REJEITADO, event);
    }

    public void publishPagamentoConfirmado(PagamentoConfirmadoEvent event) {
        log.info("Publicando pagamento.confirmado para osId={}", event.osId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.BILLING_EVENTS_EXCHANGE, RabbitMQConfig.RK_PAGAMENTO_CONFIRMADO, event);
    }
}
