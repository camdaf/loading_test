package br.com.carlos.loadingjr_test.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity 
@Table(name = "solicitacao"
)
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 


public class Solicitacao {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String titulo;

    @Column (length= 2000, nullable = false)
    private String descricao;

    @Column (nullable = false)
    private String categoria;

    @Enumerated(EnumType.STRING) 
    @Column (nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING) 
    @Column (nullable = false)
    private Status status;

    @Column (nullable = false, name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column (nullable = false, name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column (name = "data_edicao", nullable = true)
    private LocalDateTime dataEdicao;

    @PrePersist 
    void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate 
    void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
    
}
