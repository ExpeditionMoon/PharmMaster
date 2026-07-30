package com.moon.pharm.prescription.ocr

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

class TextRecognitionAnalyzer(
    private val onTextFound: (String) -> Unit,
    private val onFailure: () -> Unit,
    private val onComplete: () -> Unit = {}
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onTextFound(visionText.text)
                }
                .addOnFailureListener { onFailure() }
                .addOnCompleteListener {
                    imageProxy.close()
                    onComplete()
                }
        } else {
            imageProxy.close()
            onFailure()
            onComplete()
        }
    }
}
