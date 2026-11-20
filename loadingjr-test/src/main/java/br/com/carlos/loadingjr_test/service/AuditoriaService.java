package br.com.carlos.loadingjr_test.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carlos.loadingjr_test.database.repository.TabelaAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import br.com.carlos.loadingjr_test.dto.AuditoriaResponseDTO;

@Service 
@RequiredArgsConstructor 
public class AuditoriaService {
    private final TabelaAuditoriaRepository auditoriaRepository;

    public List<AuditoriaResponseDTO> listarTodos() {
        return auditoriaRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(AuditoriaResponseDTO::new)
                .collect(Collectors.toList());
    }
}
