package com.example.todousingcompose

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todousingcompose.screens.HomeScreen
import com.example.todousingcompose.screens.PhoneAuthScreen
import com.example.todousingcompose.ui.theme.ToDoUsingComposeTheme
import com.example.todousingcompose.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoUsingComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(activity = this)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(activity: Activity) {

    val navController = rememberNavController()
    val navHost = NavHost(
        navController = navController,
        startDestination = Routes.PHONE_AUTH_SCREEN
    ) {
        composable(Routes.PHONE_AUTH_SCREEN) {
            PhoneAuthScreen(activity = activity, navController = navController)
        }
        composable(Routes.HOME_SCREEN) {
            HomeScreen()
        }
    }

}