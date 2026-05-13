package cl.colegio.records_service.enums;

public enum EstadoAsistencia {
    PRESENTE,
    AUSENTE,
    ATRASADO,
    JUSTIFICADO;

    public static boolean esValido(String estado) {
        if (estado == null) {
            return false;
        }
        for (EstadoAsistencia e : values()) {
            if (e.name().equals(estado.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getMensajeError() {
        return "Estado de asistencia inválido. Valores válidos: PRESENTE, AUSENTE, ATRASADO, JUSTIFICADO";
    }
}