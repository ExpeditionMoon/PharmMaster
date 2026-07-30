package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.moon.pharm.consult.R
import com.moon.pharm.designsystem.theme.PharmMasterTheme
import com.moon.pharm.designsystem.theme.PharmTheme
import com.moon.pharm.designsystem.util.ThemePreviews

@Composable
fun ConsultImageItem(imageUrl: String) {
    val modifier = Modifier
        .size(100.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(PharmTheme.colors.placeholder.copy(alpha = 0.2f))
        .border(1.dp, PharmTheme.colors.placeholder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))

    if (imageUrl.isBlank()) {
        Image(
            painter = rememberVectorPainter(Icons.Default.Image),
            contentDescription = stringResource(R.string.consult_detail_image_desc),
            modifier = modifier
        )
        return
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.consult_detail_image_desc),
        modifier = modifier,
        contentScale = ContentScale.Crop,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        },
        error = {
            Image(
                painter = rememberVectorPainter(Icons.Default.BrokenImage),
                contentDescription = stringResource(R.string.consult_detail_image_desc),
                modifier = Modifier.fillMaxSize().padding(28.dp)
            )
        }
    )
}

@ThemePreviews
@Composable
private fun ConsultImageItemPreview() {
    PharmMasterTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ConsultImageItem(imageUrl = "")
        }
    }
}
