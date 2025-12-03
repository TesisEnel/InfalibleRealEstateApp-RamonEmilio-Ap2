package com.infaliblerealestate.presentation.util.validation

import javax.inject.Inject

class UsuarioValidation @Inject constructor() {

    fun validateNombre(nombre: String): ValidationResult{
        if(nombre.isBlank()){
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede estar vacío"
            )
        }else if(nombre.length < 3){
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre debe tener al menos 3 caracteres"
            )
        }else if(nombre.length > 50){
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede tener más de 50 caracteres"
            )
        }else if(!nombre.matches(Regex("^[\\p{L} ]+$"))){
            return ValidationResult(
                isValid = false,
                errorMessage = "El nombre solo puede contene    r letras"
            )
        }
        return ValidationResult(
            isValid = true
        )
    }

    fun validateApellido(apellido: String): ValidationResult{
        if(apellido.isBlank()){
            return ValidationResult(
                isValid = false,
                errorMessage = "El apellido no puede estar vacío"
            )
        }else if (apellido.length < 3) {
            return ValidationResult(
                isValid = false,
                errorMessage = "El apellido debe tener al menos 3 caracteres"
            )
        }else if(apellido.length > 50){
            return ValidationResult(
                isValid = false,
                errorMessage = "El apellido no puede tener más de 50 caracteres"
            )
        }else if(!apellido.matches(Regex("^[\\p{L} ]+$"))){
            return ValidationResult(
                isValid = false,
                errorMessage = "El apellido solo puede contener letras"
            )
        }
        return ValidationResult(
            isValid = true
        )
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult{
        if(!phoneNumber.matches(Regex("^\\d{10}$"))){
            return ValidationResult(
                isValid = false,
                errorMessage = "El número de teléfono debe tener 10 dígitos"
            )
        }else if(!phoneNumber.all { it.isDigit() }){
            return ValidationResult(
                isValid = false,
                errorMessage = "El número de teléfono solo puede contener dígitos"
            )
        }
        return ValidationResult(
            isValid = true
        )
    }


}
