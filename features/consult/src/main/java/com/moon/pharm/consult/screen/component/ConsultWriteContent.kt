package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moon.pharm.component_ui.theme.backgroundLight

@Composable
fun ConsultWriteContent(
    title: String,
    content: String,
    images: List<String>,
    isPublic: Boolean,
    isButtonEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onImageRemove: (String) -> Unit,
    onCameraClick: () -> Unit,
    onVisibilityChange: (Boolean) -> Unit,
    onNextClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .imePadding()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp, bottom = 10.dp)
    ) {
        ConsultWriteForm(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            title = title,
            content = content,
            images = images,
            onTitleChange = onTitleChange,
            onContentChange = onContentChange,
            onCameraClick = onCameraClick,
            onImageRemove = onImageRemove
        )

        ConsultWriteBottomBar(
            isPublic = isPublic,
            isButtonEnabled = isButtonEnabled,
            onVisibilityChange = onVisibilityChange,
            onNextClick = onNextClick
        )
    }
}