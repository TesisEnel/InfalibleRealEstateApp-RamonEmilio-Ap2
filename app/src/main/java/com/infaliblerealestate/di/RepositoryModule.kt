package com.infaliblerealestate.di

import com.infaliblerealestate.data.repository.CarritoRepositoryImpl
import com.infaliblerealestate.data.repository.PropiedadesRepositoryImpl
import com.infaliblerealestate.data.repository.UsuarioRepositoryImpl
import com.infaliblerealestate.dominio.repository.CarritoRepository
import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        impl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindPropiedadRepository(
        impl: PropiedadesRepositoryImpl
    ): PropiedadesRepository

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        impl: CarritoRepositoryImpl
    ): CarritoRepository
}