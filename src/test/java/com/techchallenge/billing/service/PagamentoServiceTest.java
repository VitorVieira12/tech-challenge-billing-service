package com.techchallenge.billing.service;

import com.techchallenge.billing.domain.exception.BusinessException;
import com.techchallenge.billing.domain.exception.ResourceNotFoundException;
import com.techchallenge.billing.domain.model.*;
import com.techchallenge.billing.domain.repository.PagamentoRepository;
import com.techchallenge.billing.domain.service.OrcamentoService;
import com.techchallenge.billing.domain.service.PagamentoService;
import com.techchallenge.billing.messaging.publisher.BillingEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock PagamentoRepository repository;
    @Mock OrcamentoService orcamentoService;
    @Mock BillingEventPublisher publisher;
    @InjectMocks PagamentoService service;

    private Orcamento orcamentoAprovado;

    @BeforeEach
    void setUp() {
        orcamentoAprovado = new Orcamento(1L, BigDecimal.valueOf(500), "Orçamento aprovado");
        orcamentoAprovado.setStatus(StatusOrcamento.APROVADO);
    }

    @Test
    @DisplayName("Deve gerar link de pagamento para orçamento aprovado")
    void deveGerarLinkPagamento() {
        when(orcamentoService.buscarPorId(1L)).thenReturn(orcamentoAprovado);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Pagamento pagamento = service.gerarLinkPagamento(1L);

        assertThat(pagamento.getLinkPagamento()).isNotBlank();
        assertThat(pagamento.getStatus()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    @DisplayName("Deve lançar exceção ao gerar link para orçamento não aprovado")
    void deveLancarExcecaoOrcamentoNaoAprovado() {
        Orcamento pendente = new Orcamento(1L, BigDecimal.valueOf(500), "Pendente");
        when(orcamentoService.buscarPorId(1L)).thenReturn(pendente);

        assertThatThrownBy(() -> service.gerarLinkPagamento(1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Deve confirmar pagamento pendente")
    void deveConfirmarPagamento() {
        Pagamento pagamento = new Pagamento(1L, 10L, BigDecimal.valueOf(500));
        when(repository.findByOsId(10L)).thenReturn(List.of(pagamento));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Pagamento confirmado = service.confirmarPagamento(10L);

        assertThat(confirmado.getStatus()).isEqualTo(StatusPagamento.CONFIRMADO);
        verify(publisher).publishPagamentoConfirmado(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando não há pagamento pendente")
    void deveLancarExcecaoSemPagamentoPendente() {
        when(repository.findByOsId(99L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.confirmarPagamento(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
