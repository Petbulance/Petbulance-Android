package com.example.petbulance.common.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.feature.home.homeDestination
import com.example.presentation.screen.feature.mypage.myPageDestination
import com.example.presentation.screen.nonfeature.login.loginDestination
import com.example.presentation.screen.nonfeature.splash.splashDestination
import com.example.presentation.screen.nonfeature.welcome.welcomeDestination
import com.example.presentation.utils.nav.ScreenDestinations

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenDestinations.Welcome.route,
        modifier = modifier
    ) {
        // Non-feature
        splashDestination(navController = navController)
        loginDestination(navController = navController)
        welcomeDestination(navController = navController)

        // Feature
        homeDestination(navController = navController)



        myPageDestination(navController = navController)
    }
}