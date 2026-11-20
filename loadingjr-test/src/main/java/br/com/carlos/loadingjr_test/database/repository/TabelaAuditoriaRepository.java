package br.com.carlos.loadingjr_test.database.repository;

import br.com.carlos.loadingjr_test.database.model.TabelaAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabelaAuditoriaRepository extends JpaRepository<TabelaAuditoria, Long> {
}