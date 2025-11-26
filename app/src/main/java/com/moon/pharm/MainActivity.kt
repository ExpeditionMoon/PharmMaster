package com.moon.pharm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.moon.pharm.ui.screen.EntryPointScreen
import com.moon.pharm.ui.theme.PharmMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmMasterTheme {
                EntryPointScreen()
            }
        }
    }
}
