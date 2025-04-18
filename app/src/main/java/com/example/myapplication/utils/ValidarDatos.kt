package com.example.myapplication.utils

data class ValidacionResultado(
    val esValido: Boolean,
    val mensajeError: String? = null
)

fun validarCampos(
    titulo: String,
    tipo: String,
    intervaloHoras: String,
    horasAyuno: String,
    horasComida: String
): ValidacionResultado {
    if (titulo.isBlank()) return ValidacionResultado(false, "El título no puede estar vacío")

    if (tipo.isBlank() || tipo == "Tipo") return ValidacionResultado(false, "Selecciona un tipo válido")

    if (tipo != "ayuno") {
        val intervalo = intervaloHoras.toIntOrNull()
        if (intervalo == null || intervalo <= 0)
            return ValidacionResultado(false, "El intervalo debe ser un número mayor que 0")
    }

    if (tipo == "ayuno") {
        val ayuno = horasAyuno.toIntOrNull()
        val comida = horasComida.toIntOrNull()

        if (ayuno == null || ayuno <= 0)
            return ValidacionResultado(false, "Las horas de ayuno deben ser válidas y mayores que 0")
        if (comida == null || comida <= 0)
            return ValidacionResultado(false, "Las horas de comida deben ser válidas y mayores que 0")
    }

    return ValidacionResultado(true)
}

