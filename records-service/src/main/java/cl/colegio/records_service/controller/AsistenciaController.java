package cl.colegio.records_service.controller;

import cl.colegio.records_service.dto.AsistenciaRequestDTO;
import cl.colegio.records_service.dto.AsistenciaResponseDTO;
import cl.colegio.records_service.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asistencias")
@RequiredArgsConstructor

public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(asistenciaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody AsistenciaRequestDTO dto) {
        return ResponseEntity.ok(asistenciaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AsistenciaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(asistenciaService.buscarPorEstado(estado));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<AsistenciaResponseDTO>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(asistenciaService.buscarPorFecha(inicio, fin));
    }
}
