package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.worker.WorkManagerHelper
import javax.inject.Inject

class ScheduleSyncUsuarioUseCase @Inject constructor(
    private val workManagerHelper: WorkManagerHelper
) {
    operator fun invoke(usuarioId: String) {
        workManagerHelper.scheduleOneTimeSync(usuarioId)
    }
}