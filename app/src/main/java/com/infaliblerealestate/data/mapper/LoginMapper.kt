package com.infaliblerealestate.data.mapper

import com.infaliblerealestate.data.remote.dto.login.LoginRequest
import com.infaliblerealestate.dominio.model.Login

fun Login.toDto() : LoginRequest = LoginRequest(
    email = email,
    password = password
)

fun LoginRequest.toDomain(): Login = Login (
    email = email,
    password = password
)

