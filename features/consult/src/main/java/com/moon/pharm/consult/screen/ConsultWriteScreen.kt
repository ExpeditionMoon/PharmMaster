package com.moon.pharm.consult.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import com.moon.pharm.consult.R
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import kotlinx.coroutines.launch

@Composable
fun ConsultWriteScreen(
    navController : NavController,
    viewModel: ConsultViewModel
) {
    ConsultWriteContent(
        onBackClick = { navController.popBackStack() },
        onPostClick = { title, content, images ->
            viewModel.viewModelScope.launch {
                val result = viewModel.createConsult("", title, content, images)
                if (result.isSuccess) {
                    navController.popBackStack()
                }
            }
        }
    )
}

@Composable
fun ConsultWriteContent(
    onBackClick: () -> Unit = {},
    onPostClick: (String, String, List<String>) -> Unit = { _, _, _ -> },
    onCameraClick: () -> Unit = {},
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val selectedImages = remember { mutableStateListOf<String>() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransparentHintTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = stringResource(R.string.consult_write_placeholder_title),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        Spacer(modifier = Modifier.height(10.dp))

        TransparentHintTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = stringResource(R.string.consult_write_placeholder_content),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = primaryLight,
                lineHeight = 24.sp
            ),
            singleLine = false,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 사진 첨부 영역
        PhotoAttachmentSection(
            images = selectedImages,
            onCameraClick = onCameraClick,
            onRemoveImage = { selectedImages.remove(it) }
        )
    }
}

/**
 * 하단 사진 첨부 섹션
 * - 카메라 버튼
 * - 이미지 리스트 (가로 스크롤)
 */
@Composable
fun PhotoAttachmentSection(
    images: List<String>,
    onCameraClick: () -> Unit,
    onRemoveImage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 카메라 버튼 (전체 너비)
        OutlinedButton(
            onClick = onCameraClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, primaryLight),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryLight)
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = "카메라 실행"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { /* 갤러리 열기 */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "사진 추가",
                        tint = Color.Gray
                    )
                }
            }

            items(images) { imageUrl ->
                Box(
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "삭제",
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Black)
                            .clickable { onRemoveImage(imageUrl) }
                            .padding(2.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * 기본 TextField의 밑줄이나 배경 없이 깔끔한 텍스트 입력을 위한 컴포저블
 */
@Composable
fun TransparentHintTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.5.dp,
                color = tertiaryLight,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Placeholder)
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            cursorBrush = SolidColor(primaryLight),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, name = "글쓰기 화면 미리보기")
@Composable
private fun ConsultWriteScreenPreview() {
    ConsultWriteContent(
        onBackClick = { },
        onPostClick = { _, _, _ -> }
    )
}
