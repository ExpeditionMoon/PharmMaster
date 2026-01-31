package com.moon.pharm.profile.mypage.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moon.pharm.component_ui.component.button.PharmOutlinedButton
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.R

@Composable
fun MyPageProfileCard(
    user: User,
    onEditProfileClick: () -> Unit = {}
) {
    val userTypeLabel = if (user.userType == UserType.PHARMACIST)
        stringResource(R.string.mypage_pharmacist_member)
    else
        stringResource(R.string.mypage_general_member)

    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (!user.profileImageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = user.profileImageUrl,
                            contentDescription = stringResource(R.string.signup_profile_image_description),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(Icons.Default.Face, contentDescription = null, tint = White)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "${user.nickName}${stringResource(R.string.mypage_user_suffix)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Text(text = userTypeLabel, fontSize = 14.sp, color = SecondFont)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            PharmOutlinedButton(
                onClick = onEditProfileClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.mypage_edit_info),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}