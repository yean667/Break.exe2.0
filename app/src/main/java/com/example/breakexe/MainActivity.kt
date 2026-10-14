package com.example.breakexe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.breakexe.ui.BreakScreen
import com.example.breakexe.ui.theme.BreakTheme
import com.example.breakexe.viewmodel.BreakViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BreakViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BreakTheme {
                BreakScreen(viewModel = viewModel)
            }
        }
    }
}
