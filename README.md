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

## Deploy em produção (EKS)

O deploy é feito pelo workflow `.github/workflows/ci-cd-billing-service.yml` em todo push para `main`/`fase-4`. O workflow:

1. Aplica `namespace.yaml`, `configmap.yaml`, `service.yaml`, `deployment.yaml`, `hpa.yaml`.
2. **Não aplica `k8s/secret.yaml`** — o Secret é criado/atualizado in-line pelo workflow a partir de GitHub Actions Secrets, evitando vazar credenciais no repositório.
3. Atualiza a imagem do Deployment para a SHA do commit.

### GitHub Actions Secrets necessários

| Secret | Descrição |
|---|---|
| `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN` | Push da imagem |
| `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` | Acesso ao EKS |
| `SONAR_TOKEN` / `SONAR_HOST_URL` | Análise SonarQube |
| `BILLING_DB_URL` | Ex.: `jdbc:postgresql://tech-challenge-db.xxxxxx.us-east-1.rds.amazonaws.com:5432/billing_service` |
| `BILLING_DB_USERNAME` | Usuário do RDS |
| `BILLING_DB_PASSWORD` | Senha do RDS |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | Credenciais do RabbitMQ no EKS |
| `MERCADOPAGO_ACCESS_TOKEN` | Token de produção/sandbox do Mercado Pago |

> `k8s/secret.yaml.example` é apenas um template para `docker-compose`/dev local. **Não aplique no cluster** — os valores são placeholders e iriam quebrar a conexão com o RDS (`UnknownHostException: postgres-billing`).
