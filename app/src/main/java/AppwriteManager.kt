package com.example.android2m_sher_e_panjab


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.google.android.gms.auth.api.signin.internal.Storage
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private val AppwriteManager.projectId: String
    get() {
        TODO()
    }
private val AppwriteManager.endpoint: String
    get() {
        TODO()
    }
private val AppwriteManager.bucketId: String
    get() {
        TODO()
    }

class AppwriteManager private constructor(private val context: Context){
    private val APPWRITE_PROJECT_ID = "699c31d1002bdae9525b"
    private val APPWRITE_PROJECT_NAME = "Sher-E-Panjab"
    private val APPWRITE_PUBLIC_ENDPOINT = "https://fra.cloud.appwrite.io/v1"


    private val client = Client(context)
        .setEndpoint(APPWRITE_PUBLIC_ENDPOINT)
        .setProject(APPWRITE_PROJECT_ID)

    private val storage = io.appwrite.services.Storage(client)

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppwriteManager? = null

        fun getInstance(context: Context): AppwriteManager {
            return INSTANCE ?: synchronized(this) { INSTANCE ?: AppwriteManager(context.applicationContext).also { INSTANCE = it
            } } }
    }

    suspend fun uploadImageFromUri(imageUri: Uri): String =
        withContext(Dispatchers.IO) { val file = uriToFile(imageUri)

            val uploadedFile = storage.createFile(
                bucketId = bucketId,
                fileId = ID.unique(),
                file = InputFile.fromFile(file)
            )

            "$endpoint/storage/buckets/$bucketId/files/${uploadedFile.id}/view?project=$projectId"
        }


    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI")

        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)

        FileOutputStream(tempFile).use { output -> inputStream.copyTo(output)
        }
        inputStream.close()
        return tempFile
    }
}


