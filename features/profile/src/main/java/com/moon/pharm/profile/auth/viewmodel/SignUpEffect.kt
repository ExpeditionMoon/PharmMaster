package com.moon.pharm.profile.auth.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.moon.pharm.component_ui.common.UiMessage

sealed interface SignUpEffect {
    data class ShowMessage(val message: UiMessage) : SignUpEffect
    data class MoveCamera(val latLng: LatLng) : SignUpEffect
    data object NavigateHome : SignUpEffect
}
