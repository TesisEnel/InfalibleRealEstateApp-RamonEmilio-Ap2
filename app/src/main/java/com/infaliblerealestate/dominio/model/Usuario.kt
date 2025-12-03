package com.infaliblerealestate.dominio.model

data class Usuario(
    val id: String,
    val userName: String,
    val email: String,
    val phoneNumber: String,
    val nombre: String,
    val apellido: String,
    val estadoUsuario: String,
    val rol: String,
)

