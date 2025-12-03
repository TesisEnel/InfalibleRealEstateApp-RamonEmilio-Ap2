package com.infaliblerealestate.dominio.usecase.workerhelper

import com.infaliblerealestate.worker.WorkManagerHelper
import javax.inject.Inject

class CancelSyncWorkUseCase @Inject constructor(
    private val workerHelper: WorkManagerHelper
) {
    operator  fun invoke() = workerHelper.cancelSyncWork()

}