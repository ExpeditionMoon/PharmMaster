package com.moon.pharm.prescription.ocr

import android.net.Uri

interface OcrTextExtractor {
    suspend fun extractTextFromUri(imageUri: Uri): Result<String>
}
