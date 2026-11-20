package br.com.carlos.loadingjr_test.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity 
@Table (name = "tabela_auditoria"
)
@NoArgsConstructor 
@Getter
@Setter 
public class TabelaAuditoria {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "id_solicitacao", nullable = false)
    private Long idSolicitacao;
    @Column (name = "nome_solicitacao", nullable = false)
    private String nomeSolicitacao;
    @Column (name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    @Column (name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;
    @Column (name = "data_edicao", nullable = true)
    private LocalDateTime dataEdicao;
    @Column (name = "data_exclusao", nullable = true)
    private LocalDateTime dataExclusao;
    @Enumerated(EnumType.STRING)
    @Column (name = "status_solicitacao", nullable = false)
    private Status statusSolicitacao;
    @Enumerated(EnumType.STRING)
    @Column (name = "tipo_evento", nullable = false)
    private TipoEvento tipoEvento;

    public TabelaAuditoria(Solicitacao solicitacao, TipoEvento tipoEvento) {
        this.idSolicitacao = solicitacao.getId();
        this.nomeSolicitacao = solicitacao.getTitulo();
        this.dataCriacao = solicitacao.getDataCriacao();
        this.dataAtualizacao = solicitacao.getDataAtualizacao();
        this.dataEdicao = solicitacao.getDataEdicao();
        this.tipoEvento = tipoEvento;
        this.statusSolicitacao = solicitacao.getStatus();

    }

}
