package com.example.presentation.screen.nonfeature.login

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.loginDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Login.route,
//        arguments = listOf(
//            navArgument(name = "") {
//                type = NavType.LongType
//                defaultValue = 0L
//            }
//        ) -> if route contains arguments
    ) {
        val viewModel: LoginViewModel = hiltViewModel()

        val argument: LoginArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            LoginArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        LoginScreen(
            navController = navController,
            argument = argument,
        )
    }
}