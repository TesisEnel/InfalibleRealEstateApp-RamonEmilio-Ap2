package com.infaliblerealestate.data.mapper

import com.infaliblerealestate.data.local.entities.UsuarioEntity
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioRequest
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioResponse
import com.infaliblerealestate.dominio.model.Usuario
fun Usuario.toDto() : UsuarioRequest = UsuarioRequest(
    phoneNumber = phoneNumber,
    nombre = nombre,
    apellido = apellido,
)

fun UsuarioResponse.toDomain(): Usuario = Usuario (
    id = id,
    userName = userName,
    email = email,
    phoneNumber = phoneNumber,
    nombre = nombre,
    apellido = apellido,
    estadoUsuario =  estadoUsuario,
    rol = rol
)

fun Usuario.toEntity() = UsuarioEntity(
    id = id,
    userName = userName,
    email = email,
    phoneNumber = phoneNumber,
    nombre = nombre,
    apellido = apellido,
    estadoUsuario = estadoUsuario,
    rol = rol,
    lastSync = System.currentTimeMillis(),
)

fun UsuarioEntity.toDomain() = Usuario(
    id = id,
    userName = userName,
    email = email,
    phoneNumber = phoneNumber,
    nombre = nombre,
    apellido = apellido,
    estadoUsuario = estadoUsuario,
    rol = rol
)

