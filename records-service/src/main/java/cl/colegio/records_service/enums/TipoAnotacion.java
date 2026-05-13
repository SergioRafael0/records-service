package cl.colegio.records_service.enums;

public enum TipoAnotacion {
    POSITIVA,
    NEGATIVA,
    OBSERVACION;

    public static boolean esValido(String tipo) {
        if (tipo == null) {
            return false;
        }
        for (TipoAnotacion e : values()) {
            if (e.name().equals(tipo.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getMensajeError() {
        return "Tipo de anotación inválido. Valores válidos: POSITIVA, NEGATIVA, OBSERVACION";
    }
}