package br.com.carlos.loadingjr_test.dto;
import br.com.carlos.loadingjr_test.database.model.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter 
public class SolicitacaoRequestDTO {
    
    @NotBlank (message = "O título não pode ser nulo ou vazio")
    private String titulo;
    @NotBlank (message = "A descrição não pode ser nula ou vazia")
    private String descricao;
    @NotBlank (message = "A categoria não pode ser nula ou vazia")
    private String categoria;
    @NotNull  (message = "A prioridade não pode ser nula")
    private Prioridade prioridade;


}
