# Billing Service

Microsserviço de Orçamentos e Pagamentos — FIAP Tech Challenge Fase 4

## Responsabilidades
- Gerar orçamentos ao receber evento `os.criada`
- Permitir aprovação/rejeição de orçamentos via REST
- Integrar com Mercado Pago para geração de link de pagamento
- Publicar eventos de pagamento confirmado

## Tecnologias
- Java 21 + Spring Boot 3.3.5
- PostgreSQL 15
- RabbitMQ (AMQP)
- Mercado Pago SDK
- Docker + Kubernetes

## Saga Pattern — Coreografado
Eventos consumidos: `os.criada`
Eventos publicados: `orcamento.gerado`, `orcamento.aprovado`, `orcamento.rejeitado`, `pagamento.confirmado`

## Executar localmente

```bash
docker-compose up -d
mvn spring-boot:run
```

## Swagger

http://localhost:8081/swagger-ui.html

## Testes

```bash
mvn test
```

Cobertura mínima: 80% (JaCoCo)
