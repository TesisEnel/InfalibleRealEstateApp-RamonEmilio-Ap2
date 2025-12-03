package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.worker.WorkManagerHelper
import javax.inject.Inject

class WorkManagerHelperUseCase @Inject constructor(
    val workManager: WorkManagerHelper
) {

}