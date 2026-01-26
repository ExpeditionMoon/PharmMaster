package com.moon.pharm.component_ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.secondaryLight
import com.moon.pharm.component_ui.util.MultipleEventsCutter

/**
 * 앱 메인 액션 버튼 (높이 50dp).
 * (0.3초 내 중복 클릭 무시)
 */
@Composable
fun PharmPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = primaryLight,
    contentColor: Color = White
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Button(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 리스트 아이템 내부용 작은 버튼 (56x30dp).
 * (0.3초 내 중복 클릭 무시)
 */
@Composable
fun PharmSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = secondaryLight,
    contentColor: Color = White
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    Button(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.size(width = 56.dp, height = 30.dp)
    ) {
        Text(text = text, fontSize = 13.sp)
    }
}

/**
 * 테두리만 있는 버튼 (높이 52dp).
 * (0.3초 내 중복 클릭 무시)
 */
@Composable
fun PharmOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }

    OutlinedButton(
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, primaryLight),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryLight)
    ) {
        content()
    }
}