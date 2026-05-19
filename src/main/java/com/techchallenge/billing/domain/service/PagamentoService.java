package com.techchallenge.billing.domain.service;

import com.techchallenge.billing.domain.exception.BusinessException;
import com.techchallenge.billing.domain.exception.ResourceNotFoundException;
import com.techchallenge.billing.domain.model.Orcamento;
import com.techchallenge.billing.domain.model.Pagamento;
import com.techchallenge.billing.domain.model.StatusOrcamento;
import com.techchallenge.billing.domain.model.StatusPagamento;
import com.techchallenge.billing.domain.repository.PagamentoRepository;
import com.techchallenge.billing.messaging.event.PagamentoConfirmadoEvent;
import com.techchallenge.billing.messaging.publisher.BillingEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoService {

    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);
    private final PagamentoRepository repository;
    private final OrcamentoService orcamentoService;
    private final BillingEventPublisher publisher;

    @Value("${mercadopago.access-token:sandbox-token}")
    private String mercadoPagoToken;

    public PagamentoService(PagamentoRepository repository, OrcamentoService orcamentoService,
                            BillingEventPublisher publisher) {
        this.repository = repository;
        this.orcamentoService = orcamentoService;
        this.publisher = publisher;
    }

    @Transactional
    public Pagamento gerarLinkPagamento(Long orcamentoId) {
        Orcamento orcamento = orcamentoService.buscarPorId(orcamentoId);
        if (orcamento.getStatus() != StatusOrcamento.APROVADO) {
            throw new BusinessException("Orçamento não está aprovado para pagamento.");
        }

        Pagamento pagamento = new Pagamento(orcamentoId, orcamento.getOsId(), orcamento.getValor());
        // Simulação do link de pagamento (em produção, chamar API do Mercado Pago)
        String link = "https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=SIMULADO_" + orcamentoId;
        pagamento.setLinkPagamento(link);
        repository.save(pagamento);

        log.info("Link de pagamento gerado para orcamentoId={}: {}", orcamentoId, link);
        return pagamento;
    }

    @Transactional
    public Pagamento confirmarPagamento(Long osId) {
        List<Pagamento> pagamentos = repository.findByOsId(osId);
        Pagamento pagamento = pagamentos.stream()
            .filter(p -> p.getStatus() == StatusPagamento.PENDENTE)
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Pagamento pendente não encontrado para OS: " + osId));

        pagamento.setStatus(StatusPagamento.CONFIRMADO);
        pagamento.setConfirmadoEm(LocalDateTime.now());
        repository.save(pagamento);

        publisher.publishPagamentoConfirmado(new PagamentoConfirmadoEvent(
            pagamento.getId(), pagamento.getOrcamentoId(), osId,
            pagamento.getValor(), LocalDateTime.now()
        ));
        log.info("Pagamento confirmado para osId={}", osId);
        return pagamento;
    }

    public List<Pagamento> listarPorOrcamento(Long orcamentoId) {
        return repository.findByOrcamentoId(orcamentoId);
    }
}
