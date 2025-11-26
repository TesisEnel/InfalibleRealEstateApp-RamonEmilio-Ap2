package com.infaliblerealestate.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class SyncUsuarioWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: UsuarioRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val USUARIO_ID_KEY = "usuario_id"
        const val WORK_NAME = "sync_usuario_worker"
    }

    override suspend fun doWork(): Result {
        return try {
            val usuarioId = inputData.getString(USUARIO_ID_KEY) ?: return Result.failure()

            Log.d("SyncWorker", "Iniciando sincronización para usuario: $usuarioId")

            when (val result = repository.syncUsuario(usuarioId)) {
                is Resource.Success -> {
                    Log.d("SyncWorker", "Sincronización exitosa")
                    Result.success()
                }
                is Resource.Error -> {
                    Log.e("SyncWorker", "Error: ${result.message}")
                    Result.retry()
                }
                else -> Result.failure()
            }
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error en sincronización", e)
            Result.retry()
        }
    }
}