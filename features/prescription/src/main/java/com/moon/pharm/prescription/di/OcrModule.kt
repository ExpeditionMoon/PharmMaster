package com.moon.pharm.prescription.di

import com.moon.pharm.prescription.ocr.OcrTextExtractor
import com.moon.pharm.prescription.ocr.TextRecognitionHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OcrModule {

    @Binds
    @Singleton
    abstract fun bindOcrTextExtractor(
        implementation: TextRecognitionHelper
    ): OcrTextExtractor
}
