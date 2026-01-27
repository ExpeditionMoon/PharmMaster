package com.moon.pharm.data.datasource

interface ImageDataSource {
    suspend fun uploadImage(uriString: String, userId: String): String
}