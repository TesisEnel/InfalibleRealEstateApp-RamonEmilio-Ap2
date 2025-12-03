package com.infaliblerealestate.dominio.usecase.propiedades

import android.content.Context
import android.net.Uri
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(
    private val repository: PropiedadesRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(propiedadId: Int, imageUris: List<Uri>): Resource<Unit> {
        if (imageUris.isEmpty()) {
            return Resource.Success(Unit)
        }
        val imageParts = imageUris.mapNotNull { uri ->
            try {
                val mimeType = context.contentResolver.getType(uri)

                val fileExtension = when (mimeType) {
                    "image/jpeg", "image/jpg" -> ".jpg"
                    "image/png" -> ".png"
                    else -> ".jpg"
                }

                val fileName = "upload_${System.currentTimeMillis()}$fileExtension"

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    val requestBody = bytes.toRequestBody(mimeType?.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("files", fileName, requestBody)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        return if (imageParts.isNotEmpty()) {
            repository.uploadImages(propiedadId, imageParts)
        } else {
            Resource.Error("No se pudieron procesar las imágenes seleccionadas.")
        }
    }
}
