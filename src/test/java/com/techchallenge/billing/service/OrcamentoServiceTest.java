package com.techchallenge.billing.service;

import com.techchallenge.billing.domain.exception.BusinessException;
import com.techchallenge.billing.domain.exception.ResourceNotFoundException;
import com.techchallenge.billing.domain.model.Orcamento;
import com.techchallenge.billing.domain.model.StatusOrcamento;
import com.techchallenge.billing.domain.repository.OrcamentoRepository;
import com.techchallenge.billing.domain.service.OrcamentoService;
import com.techchallenge.billing.messaging.event.OsCriadaEvent;
import com.techchallenge.billing.messaging.publisher.BillingEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock OrcamentoRepository repository;
    @Mock BillingEventPublisher publisher;
    @InjectMocks OrcamentoService service;

    private Orcamento orcamento;

    @BeforeEach
    void setUp() {
        orcamento = new Orcamento(1L, BigDecimal.valueOf(500), "Orçamento teste");
    }

    @Test
    @DisplayName("Deve gerar orçamento ao receber evento os.criada")
    void deveGerarOrcamento() {
        OsCriadaEvent event = new OsCriadaEvent(1L, "João", "ABC-1234", "Motor com ruído", LocalDateTime.now());
        when(repository.findByOsId(1L)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.gerarOrcamento(event);

        verify(repository).save(any(Orcamento.class));
        verify(publisher).publishOrcamentoGerado(any());
    }

    @Test
    @DisplayName("Não deve gerar orçamento duplicado")
    void naoDeveGerarOrcamentoDuplicado() {
        OsCriadaEvent event = new OsCriadaEvent(1L, "João", "ABC-1234", "Motor com ruído", LocalDateTime.now());
        when(repository.findByOsId(1L)).thenReturn(Optional.of(orcamento));

        service.gerarOrcamento(event);

        verify(repository, never()).save(any());
        verify(publisher, never()).publishOrcamentoGerado(any());
    }

    @Test
    @DisplayName("Deve aprovar orçamento pendente")
    void deveAprovarOrcamento() {
        when(repository.findById(1L)).thenReturn(Optional.of(orcamento));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Orcamento aprovado = service.aprovar(1L);

        assertThat(aprovado.getStatus()).isEqualTo(StatusOrcamento.APROVADO);
        verify(publisher).publishOrcamentoAprovado(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao aprovar orçamento já aprovado")
    void deveLancarExcecaoAoAprovarOrcamentoAprovado() {
        orcamento.setStatus(StatusOrcamento.APROVADO);
        when(repository.findById(1L)).thenReturn(Optional.of(orcamento));

        assertThatThrownBy(() -> service.aprovar(1L))
            .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Deve rejeitar orçamento pendente")
    void deveRejeitarOrcamento() {
        when(repository.findById(1L)).thenReturn(Optional.of(orcamento));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Orcamento rejeitado = service.rejeitar(1L, "Muito caro");

        assertThat(rejeitado.getStatus()).isEqualTo(StatusOrcamento.REJEITADO);
        verify(publisher).publishOrcamentoRejeitado(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar orçamento inexistente")
    void deveLancarExcecaoOrcamentoNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Deve listar todos os orçamentos")
    void deveListarOrcamentos() {
        when(repository.findAll()).thenReturn(List.of(orcamento));

        List<Orcamento> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
    }
}
