package br.com.carlos.loadingjr_test.dto;
import br.com.carlos.loadingjr_test.database.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter 
public class AtualizarStatus {
    @NotBlank (message = "O novo status é obrigatório")
    private Status status;
}
