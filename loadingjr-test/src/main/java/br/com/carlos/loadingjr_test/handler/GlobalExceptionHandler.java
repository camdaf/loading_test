package br.com.carlos.loadingjr_test.handler;

import br.com.carlos.loadingjr_test.exception.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de campos vazios (@NotBlank, @NotNull)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> errosCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("erro", "Dados inválidos na requisição");
        resposta.put("detalhes", errosCampos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // Captura os erros de regra de negócio (transição inválida ou ID não encontrado)
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, String>> handleRegraNegocio(RegraDeNegocioException ex) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // Captura erros quando enviam um Enum inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", "Valor inválido informado. Verifique os campos de prioridade ou status.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}