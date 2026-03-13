package com.moon.pharm.search.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moon.pharm.component_ui.component.item.PharmListItem
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.drug.Drug

@Composable
fun DrugListItem(
    drug: Drug,
    onClick: (Drug) -> Unit
) {
    PharmListItem(
        onClick = { onClick(drug) },
        leading = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PharmTheme.colors.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = PharmTheme.colors.primary.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxSize()
                )

                AsyncImage(
                    model = drug.imageUrl.ifBlank { null },
                    contentDescription = "약품 이미지",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        headline = drug.itemName,
        subhead = drug.companyName,
        supporting = drug.efficacy
    )
}

@ThemePreviews
@Composable
private fun DrugListItemPreview() {
    PharmMasterTheme {
        Column(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("1. 이미지가 있는 경우", color = PharmTheme.colors.secondFont, fontSize = 12.sp)
            DrugListItem(
                drug = Drug(
                    itemSeq = "1",
                    itemName = "타이레놀",
                    companyName = "한국얀센",
                    efficacy = "두통, 치통",
                    interaction = "술을 마신 후 복용하지 마세요.",
                    imageUrl = "https://example.com/image.png"
                ),
                onClick = {}
            )

            Text("2. 이미지가 없는 경우", color = PharmTheme.colors.secondFont, fontSize = 12.sp)
            DrugListItem(
                drug = Drug(
                    itemSeq = "2",
                    itemName = "이미지 없는 약",
                    companyName = "제약회사",
                    efficacy = "효능 정보",
                    interaction = "특이사항 없음",
                    imageUrl = ""
                ),
                onClick = {}
            )
        }
    }
}