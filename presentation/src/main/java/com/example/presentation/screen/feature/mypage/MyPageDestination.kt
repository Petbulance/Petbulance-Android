package com.example.presentation.screen.feature.mypage

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.myPageDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.MyPage.route
    ) {
        val viewModel: MyPageViewModel = hiltViewModel()

        val argument: MyPageArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            MyPageArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: MyPageData = let {
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val currentAppVersion by viewModel.currentAppVersion.collectAsStateWithLifecycle()
            val latestAppVersion by viewModel.latestAppVersion.collectAsStateWithLifecycle()

            MyPageData(
                userProfile = userProfile,
                currentAppVersion = currentAppVersion,
                latestAppVersion = latestAppVersion
            )
        }


        MyPageScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}