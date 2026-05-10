package cl.colegio.records_service.service.impl;

import cl.colegio.records_service.entity.Asistencia;
import cl.colegio.records_service.repository.AsistenciaRepository;
import cl.colegio.records_service.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    @Override
    public List<Asistencia> obtenerTodas() {
        return asistenciaRepository.findAll();
    }
}
