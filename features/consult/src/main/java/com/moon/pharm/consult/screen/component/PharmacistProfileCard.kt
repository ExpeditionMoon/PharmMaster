package com.moon.pharm.consult.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.auth.Pharmacist

@Composable
fun PharmacistProfileCard(
    pharmacist: Pharmacist?,
    pharmacistImageUrl: String?
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = PharmTheme.colors.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pharmacist != null) {
                PharmacistProfileImage(imageUrl = pharmacistImageUrl)

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = pharmacist.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = stringResource(R.string.consult_detail_no_pharmacist_info),
                    fontSize = 13.sp,
                    color = PharmTheme.colors.secondFont
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun PharmacistProfileCardPreview() {
    PharmMasterTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PharmacistProfileCard(
                pharmacist = Pharmacist(userId = "p1", name = "김약사", bio = "상담 가능", placeId = "1", pharmacyName = "달빛약국"),
                pharmacistImageUrl = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            PharmacistProfileCard(
                pharmacist = null,
                pharmacistImageUrl = null
            )
        }
    }
}