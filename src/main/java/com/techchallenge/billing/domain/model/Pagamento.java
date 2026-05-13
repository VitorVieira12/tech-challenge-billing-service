package com.techchallenge.billing.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orcamento_id", nullable = false)
    private Long orcamentoId;

    @Column(name = "os_id", nullable = false)
    private Long osId;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status = StatusPagamento.PENDENTE;

    @Column(name = "link_pagamento", length = 500)
    private String linkPagamento;

    @Column(name = "external_id", length = 200)
    private String externalId;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "confirmado_em")
    private LocalDateTime confirmadoEm;

    public Pagamento() {}

    public Pagamento(Long orcamentoId, Long osId, BigDecimal valor) {
        this.orcamentoId = orcamentoId;
        this.osId = osId;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public Long getOrcamentoId() { return orcamentoId; }
    public void setOrcamentoId(Long orcamentoId) { this.orcamentoId = orcamentoId; }
    public Long getOsId() { return osId; }
    public void setOsId(Long osId) { this.osId = osId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
    public String getLinkPagamento() { return linkPagamento; }
    public void setLinkPagamento(String linkPagamento) { this.linkPagamento = linkPagamento; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getConfirmadoEm() { return confirmadoEm; }
    public void setConfirmadoEm(LocalDateTime confirmadoEm) { this.confirmadoEm = confirmadoEm; }
}
