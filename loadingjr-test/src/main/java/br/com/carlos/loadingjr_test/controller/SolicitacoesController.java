package br.com.carlos.loadingjr_test.controller;

import br.com.carlos.loadingjr_test.dto.SolicitacaoRequestDTO;
import br.com.carlos.loadingjr_test.dto.SolicitacaoResponseDTO;
import br.com.carlos.loadingjr_test.database.model.Prioridade;
import br.com.carlos.loadingjr_test.database.model.Status;
import br.com.carlos.loadingjr_test.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor 
public class SolicitacoesController {


    private final SolicitacaoService solicitacaoService;
    
    @PostMapping 
    @ResponseStatus (HttpStatus.CREATED)
    public SolicitacaoResponseDTO criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO solicitacaoRequestDTO) {
       return solicitacaoService.criar(solicitacaoRequestDTO);
   }

   @GetMapping("/{id}")
     public SolicitacaoResponseDTO buscarPorId(@PathVariable("id") Long id) {
     return solicitacaoService.buscarPorId(id);
     }

   @GetMapping 
   @ResponseStatus (HttpStatus.OK)
    public List<SolicitacaoResponseDTO> listarSolicitacoes(
              @RequestParam(name = "status", required = false) Status status,
              @RequestParam(name = "prioridade", required = false) Prioridade prioridade) {
         if (status != null && prioridade != null) {
              return solicitacaoService.listarPorStatusEPrioridade(status, prioridade);
         } else if (status != null) {
              return solicitacaoService.listarPorStatus(status);
         } else if (prioridade != null) {
              return solicitacaoService.listarPorPrioridade(prioridade);
         } else {
              return solicitacaoService.listarTodos();
         }
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public SolicitacaoResponseDTO atualizarStatus(@PathVariable("id") Long id, @RequestParam("novoStatus") Status novoStatus) {
        return solicitacaoService.atualizarStatus(id, novoStatus);
    }

    @PutMapping("/{id}")
    @ResponseStatus (HttpStatus.OK)
    public SolicitacaoResponseDTO editar(@PathVariable("id") Long id, @Valid @RequestBody SolicitacaoRequestDTO solicitacaoRequestDTO) {
        return solicitacaoService.editar(id, solicitacaoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable("id") Long id) {
        solicitacaoService.deletar(id);
    }

}
