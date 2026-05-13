package com.techchallenge.billing.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ── Exchanges ────────────────────────────────────────────────────────────
    public static final String OS_EVENTS_EXCHANGE      = "os.events";
    public static final String BILLING_EVENTS_EXCHANGE = "billing.events";

    // ── Routing keys ─────────────────────────────────────────────────────────
    public static final String RK_OS_CRIADA            = "os.criada";
    public static final String RK_ORCAMENTO_GERADO     = "orcamento.gerado";
    public static final String RK_ORCAMENTO_APROVADO   = "orcamento.aprovado";
    public static final String RK_ORCAMENTO_REJEITADO  = "orcamento.rejeitado";
    public static final String RK_PAGAMENTO_CONFIRMADO = "pagamento.confirmado";

    // ── Queues ────────────────────────────────────────────────────────────────
    public static final String QUEUE_BILLING_OS_CRIADA = "billing.os.criada";

    @Bean TopicExchange osEventsExchange()      { return new TopicExchange(OS_EVENTS_EXCHANGE); }
    @Bean TopicExchange billingEventsExchange() { return new TopicExchange(BILLING_EVENTS_EXCHANGE); }

    @Bean Queue billingOsCriadaQueue() {
        return QueueBuilder.durable(QUEUE_BILLING_OS_CRIADA).build();
    }

    @Bean Binding bindingBillingOsCriada(Queue billingOsCriadaQueue, TopicExchange osEventsExchange) {
        return BindingBuilder.bind(billingOsCriadaQueue).to(osEventsExchange).with(RK_OS_CRIADA);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);
        return template;
    }
}
