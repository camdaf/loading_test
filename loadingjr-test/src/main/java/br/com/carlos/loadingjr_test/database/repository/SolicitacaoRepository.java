package br.com.carlos.loadingjr_test.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.carlos.loadingjr_test.database.model.Prioridade;
import br.com.carlos.loadingjr_test.database.model.Solicitacao;
import br.com.carlos.loadingjr_test.database.model.Status;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    
    List<Solicitacao> findByStatus(Status status);
    List<Solicitacao> findByPrioridade(Prioridade prioridade);
    List<Solicitacao> findByStatusAndPrioridade(Status status, Prioridade prioridade);
    List<Solicitacao> findByTituloContainingIgnoreCase(String titulo);
    
}
