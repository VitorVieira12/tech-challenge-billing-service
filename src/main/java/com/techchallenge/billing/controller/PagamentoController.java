package com.techchallenge.billing.controller;

import com.techchallenge.billing.domain.dto.PagamentoResponseDTO;
import com.techchallenge.billing.domain.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamentos", description = "Gerenciamento de pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @PostMapping("/link/{orcamentoId}")
    @Operation(summary = "Gerar link de pagamento (Mercado Pago)")
    public PagamentoResponseDTO gerarLink(@PathVariable Long orcamentoId) {
        return PagamentoResponseDTO.from(service.gerarLinkPagamento(orcamentoId));
    }

    @PostMapping("/webhook")
    @Operation(summary = "Webhook de notificação do Mercado Pago")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) {
        // Em produção: validar assinatura, extrair ID do pagamento, confirmar
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirmar/{osId}")
    @Operation(summary = "Confirmar pagamento manual (sandbox/testes)")
    public PagamentoResponseDTO confirmar(@PathVariable Long osId) {
        return PagamentoResponseDTO.from(service.confirmarPagamento(osId));
    }

    @GetMapping("/orcamento/{orcamentoId}")
    @Operation(summary = "Listar pagamentos por orçamento")
    public List<PagamentoResponseDTO> listarPorOrcamento(@PathVariable Long orcamentoId) {
        return service.listarPorOrcamento(orcamentoId).stream().map(PagamentoResponseDTO::from).toList();
    }
}
