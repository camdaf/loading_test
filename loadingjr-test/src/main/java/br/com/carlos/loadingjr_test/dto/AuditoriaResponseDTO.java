package br.com.carlos.loadingjr_test.dto;

import lombok.Getter;
import br.com.carlos.loadingjr_test.database.model.Status;
import br.com.carlos.loadingjr_test.database.model.TabelaAuditoria;
import br.com.carlos.loadingjr_test.database.model.TipoEvento;
import java.time.LocalDateTime;


@Getter 
public class AuditoriaResponseDTO {
    private Long id;
    private Long idSolicitacao;
    private String nomeSolicitacao;
    private TipoEvento tipoEvento;
    private String mensagem;
    private Status status;
    private LocalDateTime dataEvento;

    public AuditoriaResponseDTO(TabelaAuditoria a) {
        this.id = a.getId();
        this.idSolicitacao = a.getIdSolicitacao();
        this.nomeSolicitacao = a.getNomeSolicitacao();
        this.tipoEvento = a.getTipoEvento();
        this.status = a.getStatusSolicitacao();

        switch (a.getTipoEvento()) {
            case CRIACAO -> {
                this.mensagem = "Solicitação criada";
                this.dataEvento = a.getDataCriacao();
            }
            case EDICAO -> {
                this.mensagem = "Solicitação editada";
                this.dataEvento = a.getDataEdicao();
            }
            case ATUALIZACAO_STATUS -> {
                this.mensagem = "Status atualizado para " + a.getStatusSolicitacao().getDescricao();
                this.dataEvento = a.getDataAtualizacao();
            }
            case EXCLUSAO -> {
                this.mensagem = "Solicitação excluída";
                this.dataEvento = a.getDataExclusao();
            }
        }
    }
}