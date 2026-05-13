package com.techchallenge.billing.controller;

import com.techchallenge.billing.domain.dto.OrcamentoResponseDTO;
import com.techchallenge.billing.domain.service.OrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@Tag(name = "Orçamentos", description = "Gerenciamento de orçamentos")
public class OrcamentoController {

    private final OrcamentoService service;

    public OrcamentoController(OrcamentoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os orçamentos")
    public List<OrcamentoResponseDTO> listar() {
        return service.listarTodos().stream().map(OrcamentoResponseDTO::from).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orçamento por ID")
    public OrcamentoResponseDTO buscarPorId(@PathVariable Long id) {
        return OrcamentoResponseDTO.from(service.buscarPorId(id));
    }

    @GetMapping("/os/{osId}")
    @Operation(summary = "Buscar orçamento pela OS")
    public OrcamentoResponseDTO buscarPorOsId(@PathVariable Long osId) {
        return OrcamentoResponseDTO.from(service.buscarPorOsId(osId));
    }

    @PostMapping("/{id}/aprovar")
    @Operation(summary = "Aprovar orçamento")
    public OrcamentoResponseDTO aprovar(@PathVariable Long id) {
        return OrcamentoResponseDTO.from(service.aprovar(id));
    }

    @PostMapping("/{id}/rejeitar")
    @Operation(summary = "Rejeitar orçamento")
    public OrcamentoResponseDTO rejeitar(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Rejeitado pelo cliente") String motivo) {
        return OrcamentoResponseDTO.from(service.rejeitar(id, motivo));
    }
}
