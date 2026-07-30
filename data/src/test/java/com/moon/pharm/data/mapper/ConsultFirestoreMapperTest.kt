package com.moon.pharm.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConsultFirestoreMapperTest {

    @Test
    fun `current image object is not treated as a legacy image schema`() {
        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/app/o/consult_images%2Fuser%2Fnew.jpg?alt=media"
        val document = mapOf<String, Any?>(
            "images" to listOf(
                mapOf(
                    "imageName" to "new.jpg",
                    "imageUrl" to imageUrl
                )
            )
        )

        assertFalse(document.hasLegacyConsultImageUrls())
    }

    @Test
    fun `legacy image URL list is converted to image DTOs`() {
        val imageUrl = "https://firebasestorage.googleapis.com/v0/b/app/o/consult_images%2Fuser%2Flegacy.jpg?alt=media"
        val document = mapOf<String, Any?>("images" to listOf(imageUrl))

        val result = document.toLegacyConsultItemDto(documentId = "consult-id")

        assertTrue(document.hasLegacyConsultImageUrls())
        assertEquals(imageUrl, result.images?.single()?.imageUrl)
        assertEquals("consult_images%2Fuser%2Flegacy.jpg", result.images?.single()?.imageName)
    }
}
