package cl.colegio.records_service.controller;

import cl.colegio.records_service.entity.Asistencia;
import cl.colegio.records_service.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asistencias")
@RequiredArgsConstructor

public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @GetMapping
    public List<Asistencia> obtenerTodas() {
        return asistenciaService.obtenerTodas();
    }
}
