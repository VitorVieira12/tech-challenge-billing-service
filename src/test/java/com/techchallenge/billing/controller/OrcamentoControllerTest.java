package com.techchallenge.billing.controller;

import com.techchallenge.billing.domain.model.Orcamento;
import com.techchallenge.billing.domain.model.StatusOrcamento;
import com.techchallenge.billing.domain.service.OrcamentoService;
import com.techchallenge.billing.messaging.publisher.BillingEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrcamentoController.class)
@ActiveProfiles("test")
class OrcamentoControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean OrcamentoService orcamentoService;
    @MockBean BillingEventPublisher billingEventPublisher;

    @Test
    @DisplayName("GET /orcamentos deve retornar lista")
    void deveListarOrcamentos() throws Exception {
        Orcamento o = new Orcamento(1L, BigDecimal.valueOf(500), "Teste");
        when(orcamentoService.listarTodos()).thenReturn(List.of(o));

        mockMvc.perform(get("/orcamentos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].osId").value(1));
    }

    @Test
    @DisplayName("POST /orcamentos/{id}/aprovar deve retornar orçamento aprovado")
    void deveAprovarOrcamento() throws Exception {
        Orcamento o = new Orcamento(1L, BigDecimal.valueOf(500), "Teste");
        o.setStatus(StatusOrcamento.APROVADO);
        when(orcamentoService.aprovar(1L)).thenReturn(o);

        mockMvc.perform(post("/orcamentos/1/aprovar"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APROVADO"));
    }
}
