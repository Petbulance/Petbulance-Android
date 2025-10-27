package com.example.presentation.screen.nonfeature.splash

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.splashDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Splash.route
    ) {
        val viewModel: SplashViewModel = hiltViewModel()

        val argument: SplashArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            SplashArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        SplashScreen(
            navController = navController,
            argument = argument
        )
    }
}