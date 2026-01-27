package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.component.input.PrimaryTextField
import com.moon.pharm.component_ui.theme.Black
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.consult.R
import com.moon.pharm.consult.screen.section.PhotoAttachmentSection

@Composable
fun ConsultWriteForm(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    images: List<String>,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCameraClick: () -> Unit,
    onImageRemove: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        PrimaryTextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = stringResource(R.string.consult_write_placeholder_title),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(10.dp))

        PrimaryTextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = stringResource(R.string.consult_write_placeholder_content),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = primaryLight,
                lineHeight = 24.sp
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.TopStart
        )

        Spacer(modifier = Modifier.height(10.dp))

        PhotoAttachmentSection(
            images = images,
            onCameraClick = onCameraClick,
            onRemoveImage = onImageRemove
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
