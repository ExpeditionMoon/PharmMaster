package com.moon.pharm.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.R
import com.moon.pharm.component_ui.theme.OnSurface
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.Secondary
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.component.card.HealthInfoCard
import com.moon.pharm.component_ui.component.SectionHeader

@Preview(showBackground = true)
@Composable
fun HomeMainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
    ){
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(24.dp),
        ){
            /* Screen */
            Text(
                text = "ooo님, 건강 챙기세요!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )

            PharmNotice()

            RateOfUse()

            PharmSafety()

            HealthInfo()
        }
    }
}

@Composable
fun PharmNotice() {
    /* 나의 알림 */
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ){
        SectionHeader (
            title = "나의 알림"
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .border(
                    width = 0.5.dp,
                    color = Color(118,118,118).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(White),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Column (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 10.dp)
                        .background(
                            color = Secondary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(100.dp)
                        )
                ){
                    Icon(
                        Icons.Filled.Medication,
                        contentDescription = "medication",
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Column {
                    Text(
                        text = "오후 12:30",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        modifier = Modifier.padding(top = 10.dp),
                        text = "점심약 복용 시간입니다",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = SecondFont
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, bottom = 20.dp),
                text = "총 2개 알림",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(51, 51, 51)
            )
        }
    }
}

@Composable
fun RateOfUse() {
    /* 복용률 */
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = Secondary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(10.dp)
            )
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier = Modifier
                .padding(10.dp)
                .border(
                    width = 1.dp,
                    color = Secondary,
                    shape = RoundedCornerShape(10.dp)
                )
                .height(40.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = "이번 주 복용률",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "95%",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = SecondFont
            )
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ){
            Text(
                text = "남은 약물 3일분 💊",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "오전 8:00 아침약 복용 완료",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(118, 118, 118)
            )
        }
    }
}

@Composable
fun PharmSafety() {
    /* DDI / ADR */
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(130.dp)
            .background(
                color = Primary,
                shape = RoundedCornerShape(10.dp)
            ),
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                Icons.Outlined.VerifiedUser,
                contentDescription = "verifiedUser",
                tint = Color.White,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Column (
                modifier = Modifier
                    .padding(end = 20.dp),
            ){
                Text(
                    text = "복용 전 약물 안전성을 확인하세요!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "복용 중인 약물 간 상호작용(DDI) 위험을 점검하고 건강을 챙기세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFDDDDDD)
                )
            }
        }
        Column (
            modifier = Modifier
                .width(325.dp)
                .height(30.dp)
                .padding(top = 10.dp, start = 65.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(5.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TextButton (
                onClick = {},
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Primary
                ),
                contentPadding = PaddingValues(0.dp)
            ){
                Text(
                    text = "안전성 바로 확인하기 >",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun HealthInfo() {
    /* 건강 정보 */
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        SectionHeader (
            title = "건강 정보"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HealthInfoCard(
                imageResId = R.drawable.health_info1,
                title = "올바른 약 복용법",
                description = "물과 함께 복용하는 것이 가장 좋습니다."
            )
            HealthInfoCard(
                imageResId = R.drawable.health_info2,
                title = "의약품 보관법",
                description = "의약품은 직사광선이 닿지 않는 서늘하고 ..."
            )
            HealthInfoCard(
                imageResId = R.drawable.health_info3,
                title = "유통기한 확인",
                description = "유통기한이 지난 약은 폐기해야 합니다."
            )
        }
    }
}
