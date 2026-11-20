package br.com.carlos.loadingjr_test.controller;

import br.com.carlos.loadingjr_test.service.AuditoriaService;
import br.com.carlos.loadingjr_test.dto.AuditoriaResponseDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/auditoria")
@RequiredArgsConstructor 
public class AuditoriaController {
    private final AuditoriaService auditoriaService;

    @GetMapping 
    public List<AuditoriaResponseDTO> listar() {
        return auditoriaService.listarTodos();
    }
}