package com.moon.pharm.component_ui.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.pharm.component_ui.theme.PharmMasterTheme
import com.moon.pharm.component_ui.theme.PharmTheme
import com.moon.pharm.component_ui.util.ThemePreviews

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(PharmTheme.colors.surface, RoundedCornerShape(10.dp))
            .border(0.5.dp, PharmTheme.colors.tertiary, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = PharmTheme.colors.placeholder,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(color = PharmTheme.colors.placeholder, fontSize = 14.sp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    interactionSource = interactionSource,
                    textStyle = TextStyle(color = PharmTheme.colors.onSurface, fontSize = 14.sp),
                    singleLine = true,
                    cursorBrush = SolidColor(PharmTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SearchBarPreview() {
    PharmMasterTheme {
        Column(
            modifier = Modifier
                .background(PharmTheme.colors.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = "약국 이름이나 주소를 검색해보세요"
            )
            SearchBar(
                value = "강남역",
                onValueChange = {},
                placeholder = "약국 이름이나 주소를 검색해보세요"
            )
        }
    }
}