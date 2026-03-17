package com.moon.pharm.component_ui.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.domain.model.auth.Pharmacist

@Composable
fun PharmacistListItem(
    pharmacist: Pharmacist,
    onSelect: (Pharmacist) -> Unit,
    modifier: Modifier = Modifier,
    btnText: String? = null
) {
    val textToDisplay = btnText ?: stringResource(R.string.btn_select)

    PharmListItem(
        modifier = modifier,
        leading = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PharmTheme.colors.tertiary)
                    .border(1.dp, PharmTheme.colors.tertiary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = PharmTheme.colors.placeholder,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        headline = pharmacist.name,
        subhead = pharmacist.bio,
        trailing = {
            Button(
                onClick = { onSelect(pharmacist) },
                colors = ButtonDefaults.buttonColors(containerColor = PharmTheme.colors.primary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(36.dp)
                    .width(64.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = textToDisplay,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@ThemePreviews
@Composable
private fun PharmacistListItemPreview() {
    PharmMasterTheme {
        Box(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp)
        ) {
            PharmacistListItem(
                pharmacist = Pharmacist(
                    userId = "user_12345",
                    name = "김약사",
                    bio = "복약 지도를 꼼꼼하게 해드립니다.",
                    placeId = "place_001",
                    pharmacyName = "달빛약국",
                    isApproved = true
                ),
                onSelect = {},
                btnText = "선택하기"
            )
        }
    }
}