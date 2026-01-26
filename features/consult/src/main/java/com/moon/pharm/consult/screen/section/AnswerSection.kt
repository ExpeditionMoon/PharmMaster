package com.moon.pharm.consult.screen.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.util.toDisplayDateTimeString
import com.moon.pharm.consult.R
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultItem

@Composable
fun AnswerSection(
    pharmacist: Pharmacist?,
    pharmacistImageUrl: String?,
    item: ConsultItem
) {
    val answer = item.answer ?: return

    Column {
        Text(
            text = stringResource(R.string.consult_detail_answer_section),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pharmacist != null) {
                    if (!pharmacistImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = pharmacistImageUrl,
                            contentDescription = stringResource(R.string.consult_detail_pharmacist_profile_desc),
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            error = rememberVectorPainter(Icons.Default.Person),
                            placeholder = rememberVectorPainter(Icons.Default.Person)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = pharmacist.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.consult_detail_no_pharmacist_info),
                        fontSize = 13.sp,
                        color = SecondFont
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = answer.content,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(
                        R.string.consult_detail_answer_date_format,
                        answer.createdAt.toDisplayDateTimeString()
                    ),
                    fontSize = 12.sp,
                    color = SecondFont
                )
            }
        }
    }
}