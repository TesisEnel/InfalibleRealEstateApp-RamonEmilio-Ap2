package com.infaliblerealestate.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String,
    val userName: String,
    val email: String,
    val phoneNumber: String,
    val nombre: String,
    val apellido: String,
    val estadoUsuario: String,
    val rol: String,
    val lastSync: Long = 0L,
)

