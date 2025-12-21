package com.moon.pharm.consult.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.moon.pharm.consult.R
import com.moon.pharm.component_ui.theme.Placeholder
import com.moon.pharm.component_ui.theme.Primary
import com.moon.pharm.component_ui.theme.SecondFont
import com.moon.pharm.component_ui.theme.White
import com.moon.pharm.component_ui.theme.backgroundLight
import com.moon.pharm.component_ui.theme.primaryLight
import com.moon.pharm.component_ui.theme.tertiaryLight
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.model.Pharmacist
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultPharmacistScreen(
    navController: NavController,
    viewModel: ConsultViewModel,
    onPharmacistSelected: (Pharmacist) -> Unit = {}
) {
    val pharmacists by viewModel.pharmacists.collectAsState()
    var isMapView by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    BackHandler {
        if (isMapView) isMapView = false else navController.popBackStack()
    }

    ConsultPharmacistContent(
        isMapView = isMapView,
        searchText = searchText,
        pharmacists = pharmacists,
        onSearchChange = {
            searchText = it
            viewModel.selectPharmacy(it) },
        onToggleMapView = { isMapView = it },
        onPharmacistSelect = { pharmacist ->
            onPharmacistSelected(pharmacist)
            navController.popBackStack()
        }
    )
}

@Composable
fun ConsultPharmacistContent(
    isMapView: Boolean,
    searchText: String,
    pharmacists: List<Pharmacist>,
    onSearchChange: (String) -> Unit,
    onToggleMapView: (Boolean) -> Unit,
    onPharmacistSelect: (Pharmacist) -> Unit
) {
    if (isMapView) {
        PharmacistMapView(
            pharmacists = pharmacists,
            onBack = { onToggleMapView(false) },
            onPharmacistSelect = onPharmacistSelect
        )
    } else {
        PharmacistSearchView(
            searchText = searchText,
            pharmacists = pharmacists,
            onSearchChange = onSearchChange,
            onNavigateToMap = { onToggleMapView(true) },
            onPharmacistSelect = onPharmacistSelect
        )
    }
}

@Composable
fun PharmacistItem(
    pharmacist: Pharmacist,
    onSelect: (Pharmacist) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(tertiaryLight)
                    .border(1.dp, tertiaryLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (pharmacist.imageUrl.isNotEmpty()) {
//                    Image(
//                        painter = painterResource(id = pharmacist.imageUrl),
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize()
//                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = pharmacist.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pharmacist.bio,
                    fontSize = 13.sp,
                    color = SecondFont
                )
            }
        }

        Button(
            onClick = { onSelect(pharmacist) },
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(36.dp)
                .width(64.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "선택",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun PharmacistSearchView(
    searchText: String,
    pharmacists: List<Pharmacist>,
    onSearchChange: (String) -> Unit,
    onNavigateToMap: () -> Unit,
    onPharmacistSelect: (Pharmacist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.consult_search_select_pharmacist),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SearchBar(
            value = searchText,
            onValueChange = onSearchChange,
            placeholder = stringResource(R.string.consult_search_placeholder)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(text = stringResource(R.string.consult_search_nearby), isSelected = true)
            FilterChip(text = stringResource(R.string.consult_search_favorite), isSelected = false)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchText.isNotEmpty()) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items (pharmacists){ pharmacist ->
                    PharmacistItem(
                        pharmacist = pharmacist,
                        onSelect = onPharmacistSelect
                    )
                }
            }
        } else {
            MapFindBanner(onClick = onNavigateToMap)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacistMapView(
    pharmacists: List<Pharmacist>,
    onBack: () -> Unit,
    onPharmacistSelect: (Pharmacist) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BackHandler {
        onBack()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(tertiaryLight)
                    )
                }
                
                Text(
                    text = "OO약국 약사 목록",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(pharmacists) { pharmacist ->
                        PharmacistItem(
                            pharmacist = pharmacist,
                            onSelect = onPharmacistSelect
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
//            Image(
//                painter = painterResource(id = R.drawable.map_image),
//                contentDescription = "지도",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
            
            Button(
                onClick = {
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "약국 선택하기 (시뮬레이션)")
            }
        }
    }
}


@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(White, RoundedCornerShape(10.dp))
            .border(0.5.dp, tertiaryLight, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Placeholder,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = TextStyle(color = Placeholder, fontSize = 14.sp)
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    singleLine = true,
                    cursorBrush = SolidColor(primaryLight),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Primary else White)
            .border(
                width = if (isSelected) 0.dp else 0.5.dp,
                color = if (isSelected) Color.Transparent else tertiaryLight,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) White else Color.Gray
        )
    }
}

@Composable
fun MapFindBanner(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF3E5F5)  
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "지도",
                tint = Primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "지도를 열어 약사 찾기",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PharmacistSearchViewPreview() {
    val fakePharmacists = listOf(
        Pharmacist("1", "김약사", "", "만성질환 상담 전문"),
        Pharmacist("2", "이약사", "", "비타민/영양제 상담 전문")
    )

    Column(modifier = Modifier.fillMaxSize().background(backgroundLight).padding(20.dp)) {
        PharmacistSearchView(
            searchText = "",
            pharmacists = fakePharmacists,
            onSearchChange = {},
            onNavigateToMap = {},
            onPharmacistSelect = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        PharmacistItem(pharmacist = fakePharmacists[0], onSelect = {})
    }
}