package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.AppViewModel
import com.example.ui.screens.DashboardContainer
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.ironsource.mediationsdk.IronSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val appViewModel: AppViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "loading"
                    ) {
                        // Loading / Splash Screen
                        composable("loading") {
                            SplashScreen(
                                onNavigateToLogin = {
                                    val destination = if (appViewModel.isLoggedIn.value) "dashboard" else "signin"
                                    navController.navigate(destination) {
                                        popUpTo("loading") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Login Screen
                        composable("signin") {
                            LoginScreen(
                                viewModel = appViewModel,
                                onNavigateToRegister = {
                                    navController.navigate("signup")
                                },
                                onLoginSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("signin") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Register Screen
                        composable("signup") {
                            RegisterScreen(
                                viewModel = appViewModel,
                                onNavigateToLogin = {
                                    navController.navigate("signin") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                },
                                onRegisterSuccess = {
                                    navController.navigate("dashboard") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Dashboard Screen
                        composable("dashboard") {
                            DashboardContainer(
                                viewModel = appViewModel,
                                onLogout = {
                                    navController.navigate("signin") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }
}
