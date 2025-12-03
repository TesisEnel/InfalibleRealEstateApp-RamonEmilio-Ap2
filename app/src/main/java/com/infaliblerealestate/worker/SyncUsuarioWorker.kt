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
            val usuarioId = inputData.getString(USUARIO_ID_KEY)
                ?: return Result.failure()

            when (val result = repository.syncUsuarioToRemote(usuarioId)) {
                is Resource.Success -> {
                    Result.success()
                }
                is Resource.Error -> {
                    if (runAttemptCount < 3) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
                else -> {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}