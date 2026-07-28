package com.moon.pharm.component_ui.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun PharmListItem(
    modifier: Modifier = Modifier,
    containerColor: Color = PharmTheme.colors.surface,
    borderColor: Color? = null,
    contentPadding: Dp = 16.dp,
    leading: @Composable (() -> Unit)? = null,
    headline: String,
    subhead: String? = null,
    supporting: String? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val borderModifier = if (borderColor != null) {
        Modifier.border(1.dp, borderColor, RoundedCornerShape(10.dp))
    } else Modifier

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .then(borderModifier)
            .background(containerColor)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            leading()
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = headline,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PharmTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subhead != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subhead,
                    fontSize = 13.sp,
                    color = PharmTheme.colors.secondFont,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (supporting != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = supporting,
                    fontSize = 12.sp,
                    color = PharmTheme.colors.secondFont.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (trailing != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
    }
}

@ThemePreviews
@Composable
private fun PharmListItemPreview() {
    PharmMasterTheme {
        Column(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("1. 의약품 검색 스타일", fontSize = 12.sp, color = PharmTheme.colors.secondFont)
            PharmListItem(
                leading = {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PharmTheme.colors.tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            tint = PharmTheme.colors.secondFont
                        )
                    }
                },
                headline = "타이레놀정500밀리그람",
                subhead = "(주)한국얀센",
                supporting = "감기로 인한 발열 및 통증, 두통, 신경통, 근육통, 월경통, 염좌통",
                onClick = {}
            )

            Text("2. 약국/장소 스타일", fontSize = 12.sp, color = PharmTheme.colors.secondFont)
            PharmListItem(
                leading = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PharmTheme.colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                headline = "달빛약국",
                subhead = "서울특별시 강남구 테헤란로 123",
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = PharmTheme.colors.placeholder
                    )
                },
                onClick = {}
            )

            Text("3. 단순 정보 스타일", fontSize = 12.sp, color = PharmTheme.colors.secondFont)
            PharmListItem(
                headline = "최근 검색어",
                subhead = "2024.03.14",
                onClick = {}
            )
        }
    }
}