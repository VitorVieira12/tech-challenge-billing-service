package com.techchallenge.billing.domain.service;

import com.techchallenge.billing.domain.exception.BusinessException;
import com.techchallenge.billing.domain.exception.ResourceNotFoundException;
import com.techchallenge.billing.domain.model.Orcamento;
import com.techchallenge.billing.domain.model.StatusOrcamento;
import com.techchallenge.billing.domain.repository.OrcamentoRepository;
import com.techchallenge.billing.messaging.event.*;
import com.techchallenge.billing.messaging.publisher.BillingEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OrcamentoService {

    private static final Logger log = LoggerFactory.getLogger(OrcamentoService.class);
    private final OrcamentoRepository repository;
    private final BillingEventPublisher publisher;
    private final Random random = new Random();

    public OrcamentoService(OrcamentoRepository repository, BillingEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public void gerarOrcamento(OsCriadaEvent event) {
        if (repository.findByOsId(event.osId()).isPresent()) {
            log.warn("Orçamento já existe para osId={}", event.osId());
            return;
        }
        BigDecimal valor = BigDecimal.valueOf(200 + random.nextInt(800));
        String descricao = "Orçamento gerado automaticamente para OS #" + event.osId()
            + " - Problema: " + event.descricaoProblema();

        Orcamento orcamento = new Orcamento(event.osId(), valor, descricao);
        repository.save(orcamento);

        publisher.publishOrcamentoGerado(new OrcamentoGeradoEvent(
            orcamento.getId(), event.osId(), valor, descricao, LocalDateTime.now()
        ));
        log.info("Orçamento gerado: id={}, osId={}, valor={}", orcamento.getId(), event.osId(), valor);
    }

    public List<Orcamento> listarTodos() {
        return repository.findAll();
    }

    public Orcamento buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Orçamento não encontrado: " + id));
    }

    public Orcamento buscarPorOsId(Long osId) {
        return repository.findByOsId(osId)
            .orElseThrow(() -> new ResourceNotFoundException("Orçamento não encontrado para OS: " + osId));
    }

    @Transactional
    public Orcamento aprovar(Long id) {
        Orcamento orcamento = buscarPorId(id);
        if (orcamento.getStatus() != StatusOrcamento.PENDENTE) {
            throw new BusinessException("Orçamento não está pendente. Status atual: " + orcamento.getStatus());
        }
        orcamento.setStatus(StatusOrcamento.APROVADO);
        repository.save(orcamento);

        publisher.publishOrcamentoAprovado(new OrcamentoAprovadoEvent(
            orcamento.getId(), orcamento.getOsId(), orcamento.getValor(), LocalDateTime.now()
        ));
        log.info("Orçamento aprovado: id={}, osId={}", id, orcamento.getOsId());
        return orcamento;
    }

    @Transactional
    public Orcamento rejeitar(Long id, String motivo) {
        Orcamento orcamento = buscarPorId(id);
        if (orcamento.getStatus() != StatusOrcamento.PENDENTE) {
            throw new BusinessException("Orçamento não está pendente. Status atual: " + orcamento.getStatus());
        }
        orcamento.setStatus(StatusOrcamento.REJEITADO);
        repository.save(orcamento);

        publisher.publishOrcamentoRejeitado(new OrcamentoRejeitadoEvent(
            orcamento.getId(), orcamento.getOsId(), motivo, LocalDateTime.now()
        ));
        log.info("Orçamento rejeitado: id={}, osId={}, motivo={}", id, orcamento.getOsId(), motivo);
        return orcamento;
    }
}
