package com.moon.pharm.profile.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.moon.pharm.component_ui.model.ScannedMedication
import kotlinx.serialization.json.Json

val ScannedMedicationListNavType = object : NavType<List<ScannedMedication>>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): List<ScannedMedication>? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): List<ScannedMedication> {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: List<ScannedMedication>) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun serializeAsValue(value: List<ScannedMedication>): String {
        return Uri.encode(Json.encodeToString(value))
    }
}