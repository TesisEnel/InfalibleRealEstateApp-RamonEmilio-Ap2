package com.infaliblerealestate.presentation.util.validation

import javax.inject.Inject

class PropiedadesValidation @Inject constructor() {

    fun validateTitulo(titulo: String): ValidationResult {
        if (titulo.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El título no puede estar vacío"
            )
        } else if (titulo.length < 5) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El título debe tener al menos 5 caracteres"
            )
        } else if (titulo.length > 100) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El título no puede tener más de 100 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validatePrecio(precio: Double): ValidationResult {
        if (precio <= 0) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El precio debe ser mayor a 0"
            )
        } else if (precio > 999999999) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El precio es demasiado alto"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateMoneda(moneda: String): ValidationResult {
        if (moneda.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Debe seleccionar una moneda"
            )
        } else if (moneda !in listOf("Peso", "Dolar")) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Moneda no válida"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateCiudad(ciudad: String): ValidationResult {
        if (ciudad.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La ciudad no puede estar vacía"
            )
        } else if (ciudad.length < 2) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La ciudad debe tener al menos 2 caracteres"
            )
        } else if (ciudad.length > 80) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La ciudad no puede tener más de 80 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateEstadoProvincia(estadoProvincia: String): ValidationResult {
        if (estadoProvincia.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La provincia no puede estar vacía"
            )
        } else if (estadoProvincia.length < 2) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La provincia debe tener al menos 2 caracteres"
            )
        } else if (estadoProvincia.length > 80) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La provincia no puede tener más de 80 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateTipoTransaccion(tipoTransaccion: String): ValidationResult {
        if (tipoTransaccion.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Debe seleccionar un tipo de transacción"
            )
        } else if (tipoTransaccion !in listOf("Venta", "Alquiler")) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Tipo de transacción no válido"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateCategoria(categoriaId: Int): ValidationResult {
        if (categoriaId <= 0) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Debe seleccionar una categoría"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateEstadoPropiedad(estadoPropiedadId: Int): ValidationResult {
        if (estadoPropiedadId <= 0) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Debe seleccionar el estado de la propiedad"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateDescripcion(descripcion: String): ValidationResult {
        if (descripcion.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La descripción no puede estar vacía"
            )
        } else if (descripcion.length < 20) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La descripción debe tener al menos 20 caracteres"
            )
        } else if (descripcion.length > 1000) {
            return ValidationResult(
                isValid = false,
                errorMessage = "La descripción no puede tener más de 1000 caracteres"
            )
        }
        return ValidationResult(isValid = true)
    }

    fun validateMetrosCuadrados(metrosCuadrados: Double): ValidationResult {
         if (metrosCuadrados > 100000) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Los metros cuadrados son demasiado altos"
            )
        }
        return ValidationResult(isValid = true)
    }
}
