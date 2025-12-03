package com.infaliblerealestate.dominio.usecase.workerhelper

import com.infaliblerealestate.worker.WorkManagerHelper
import jakarta.inject.Inject

class ScheduleSyncWorkUseCase @Inject constructor(
    private val workManagerHelper: WorkManagerHelper
) {
    operator fun invoke(id: String) = workManagerHelper.scheduleSyncWork(id)
}