package br.com.carlos.loadingjr_test.dto;

import java.time.LocalDateTime;

import br.com.carlos.loadingjr_test.database.model.Prioridade;
import br.com.carlos.loadingjr_test.database.model.Solicitacao;
import br.com.carlos.loadingjr_test.database.model.Status;
import lombok.Getter;

@Getter
public class SolicitacaoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private Prioridade prioridade;
    private String prioridadeDescricao;
    private Status status;
    private String statusDescricao;
    private Status proximoStatus;
    private String proximoStatusDescricao;
    private LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;
    private LocalDateTime editadaEm;

    public SolicitacaoResponseDTO(Solicitacao s) {
        this.id = s.getId();
        this.titulo = s.getTitulo();
        this.descricao = s.getDescricao();
        this.categoria = s.getCategoria();
        this.prioridade = s.getPrioridade();
        this.prioridadeDescricao = s.getPrioridade().getDescricao();
        this.status = s.getStatus();
        this.statusDescricao = s.getStatus().getDescricao();
        this.proximoStatus = s.getStatus().proximo();
        this.proximoStatusDescricao = proximoStatus == null ? null : proximoStatus.getDescricao();
        this.criadaEm = s.getDataCriacao();
        this.atualizadaEm = s.getDataAtualizacao();
        this.editadaEm = s.getDataEdicao();
    }
}