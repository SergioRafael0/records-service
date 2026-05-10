package cl.colegio.records_service.controller;

import cl.colegio.records_service.dto.AnotacionRequestDTO;
import cl.colegio.records_service.dto.AnotacionResponseDTO;
import cl.colegio.records_service.service.AnotacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/anotaciones")
@RequiredArgsConstructor

public class AnotacionController {

    private final AnotacionService anotacionService;

    @GetMapping
    public ResponseEntity<List<AnotacionResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(anotacionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnotacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(anotacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AnotacionResponseDTO> crear(@Valid @RequestBody AnotacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anotacionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnotacionResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody AnotacionRequestDTO dto) {
        return ResponseEntity.ok(anotacionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        anotacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AnotacionResponseDTO>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(anotacionService.buscarPorTipo(tipo));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<AnotacionResponseDTO>> buscarPorEstudiante(@PathVariable Long idEstudiante) {
        return ResponseEntity.ok(anotacionService.buscarPorEstudiante(idEstudiante));
    }
}