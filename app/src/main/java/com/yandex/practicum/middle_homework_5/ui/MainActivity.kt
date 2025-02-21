package com.yandex.practicum.middle_homework_5.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.yandex.practicum.middle_homework_5.ui.navigation.BottomBar
import com.yandex.practicum.middle_homework_5.ui.navigation.BottomBarNavGraph
import com.yandex.practicum.middle_homework_5.ui.theme.WebMockServerTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebMockServerTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController)
                    }
                ) {
                    BottomBarNavGraph(navController = navController)
                }
            }
        }
    }
}


