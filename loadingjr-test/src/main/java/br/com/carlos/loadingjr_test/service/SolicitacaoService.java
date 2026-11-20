package br.com.carlos.loadingjr_test.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.carlos.loadingjr_test.database.model.Prioridade;
import br.com.carlos.loadingjr_test.database.model.Solicitacao;
import br.com.carlos.loadingjr_test.database.model.Status;
import br.com.carlos.loadingjr_test.database.model.TabelaAuditoria;
import br.com.carlos.loadingjr_test.database.model.TipoEvento;
import br.com.carlos.loadingjr_test.database.repository.SolicitacaoRepository;
import br.com.carlos.loadingjr_test.database.repository.TabelaAuditoriaRepository;
import br.com.carlos.loadingjr_test.dto.SolicitacaoResponseDTO;
import br.com.carlos.loadingjr_test.dto.SolicitacaoRequestDTO;
import br.com.carlos.loadingjr_test.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class SolicitacaoService {
    
    private final SolicitacaoRepository solicitacaoRepository;
    private final TabelaAuditoriaRepository auditoriaRepository;

    @Transactional 
    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO solicitacaoRequestDTO) {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setTitulo(solicitacaoRequestDTO.getTitulo());
        solicitacao.setDescricao(solicitacaoRequestDTO.getDescricao());
        solicitacao.setCategoria(solicitacaoRequestDTO.getCategoria());
        solicitacao.setPrioridade(solicitacaoRequestDTO.getPrioridade());
        solicitacao.setStatus(Status.ABERTA); 
        Solicitacao solicitacaoSalva = solicitacaoRepository.saveAndFlush(solicitacao);
        auditoriaRepository.save(new TabelaAuditoria(solicitacaoSalva, TipoEvento.CRIACAO));
        return new SolicitacaoResponseDTO(solicitacaoSalva);

    }

    public List<SolicitacaoResponseDTO> listarTodos() {
        return solicitacaoRepository.findAll().stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<SolicitacaoResponseDTO> listarPorStatus(Status status) {
        return solicitacaoRepository.findByStatus(status).stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<SolicitacaoResponseDTO> listarPorPrioridade(Prioridade prioridade) {
        return solicitacaoRepository.findByPrioridade(prioridade).stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<SolicitacaoResponseDTO> listarPorStatusEPrioridade(Status status, Prioridade prioridade) {
        return solicitacaoRepository.findByStatusAndPrioridade(status, prioridade).stream()
                .map(SolicitacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional 
    public SolicitacaoResponseDTO atualizarStatus(Long id, Status novoStatus){
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Solicitação não encontrada com o ID: " + id));
        
        switch (solicitacao.getStatus()){
            case ABERTA:
                if (novoStatus != Status.EM_ANDAMENTO){
                    throw new RegraDeNegocioException("Status inválido. A solicitação aberta só pode ser atualizada para EM_ANDAMENTO.");
                }
                break;
            case EM_ANDAMENTO:
                if (novoStatus != Status.CONCLUIDA){
                    throw new RegraDeNegocioException("Status inválido. A solicitação em andamento só pode ser atualizada para CONCLUIDA.");
                }
                break;
            case CONCLUIDA:
                throw new RegraDeNegocioException("Status inválido. A solicitação concluída não pode ser atualizada.");
        
        }
        
        solicitacao.setStatus(novoStatus);
        Solicitacao solicitacaoAtualizada = solicitacaoRepository.saveAndFlush(solicitacao);
        
        auditoriaRepository.save(new TabelaAuditoria(solicitacaoAtualizada, TipoEvento.ATUALIZACAO_STATUS));

        return new SolicitacaoResponseDTO(solicitacaoAtualizada);

    }
    
    public SolicitacaoResponseDTO buscarPorId(Long id) {
    Solicitacao solicitacao = solicitacaoRepository.findById(id)
            .orElseThrow(() -> new RegraDeNegocioException("Solicitação não encontrada com o ID: " + id));
        return new SolicitacaoResponseDTO(solicitacao);
    }
    @Transactional 
    public SolicitacaoResponseDTO editar(Long id, SolicitacaoRequestDTO solicitacaoRequestDTO){
        
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Solicitação não encontrada com o ID: " + id));

        if (solicitacao.getStatus() == Status.CONCLUIDA) {
            throw new RegraDeNegocioException("Solicitação concluída não pode ser editada.");
        }
        solicitacao.setTitulo(solicitacaoRequestDTO.getTitulo());
        solicitacao.setDescricao(solicitacaoRequestDTO.getDescricao());
        solicitacao.setCategoria(solicitacaoRequestDTO.getCategoria());
        solicitacao.setPrioridade(solicitacaoRequestDTO.getPrioridade());
        solicitacao.setDataEdicao(java.time.LocalDateTime.now());
        solicitacaoRepository.saveAndFlush(solicitacao);
        auditoriaRepository.save(new TabelaAuditoria(solicitacao, TipoEvento.EDICAO));
        return new SolicitacaoResponseDTO(solicitacao);
    }
   
    @Transactional
    public void deletar(Long id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Solicitação não encontrada com o ID: " + id));
        TabelaAuditoria auditoria = new TabelaAuditoria(solicitacao, TipoEvento.EXCLUSAO);
        auditoria.setDataExclusao(LocalDateTime.now());
        
        auditoriaRepository.save(auditoria);
        solicitacaoRepository.delete(solicitacao);
    }



}
