package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.moon.pharm.component_ui.component.button.PharmOutlinedButton
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.consult.R

@Composable
fun PhotoAttachmentSection(
    images: List<String>,
    onCameraClick: () -> Unit,
    onRemoveImage: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        PharmOutlinedButton(
            onClick = onCameraClick
        ) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = stringResource(R.string.consult_photo_camera_desc)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (images.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(images) { imageUrl ->
                    Box(
                        modifier = Modifier.size(width = 88.dp, height = 88.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(R.string.consult_photo_attached_desc),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.LightGray)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.consult_photo_delete_desc),
                            tint = White,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Primary)
                                .clickable { onRemoveImage(imageUrl) }
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}