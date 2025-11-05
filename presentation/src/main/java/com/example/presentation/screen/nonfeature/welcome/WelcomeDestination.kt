package com.example.presentation.screen.nonfeature.welcome

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.screen.nonfeature.terms.TermsArgument
import com.example.presentation.screen.nonfeature.terms.TermsData
import com.example.presentation.screen.nonfeature.terms.TermsViewModel
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.welcomeDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.Welcome.route
    ) {
        val viewModel: WelcomeViewModel = hiltViewModel()
        val termsViewModel: TermsViewModel = hiltViewModel()

        val welcomeArgument: WelcomeArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            WelcomeArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val welcomeData: WelcomeData = let {
            val tempUserName by viewModel.tempUserName.collectAsStateWithLifecycle()

            WelcomeData(
                tempNickname = tempUserName
            )
        }


        val termsArgument: TermsArgument = let {
            val state by termsViewModel.state.collectAsStateWithLifecycle()
            TermsArgument(
                state = state,
                intent = termsViewModel::onIntent,
                event = termsViewModel.eventFlow
            )
        }
        val termsData: TermsData = let {
            val termList by termsViewModel.termList.collectAsStateWithLifecycle()
            val termDetails by termsViewModel.termDetails.collectAsStateWithLifecycle()
            val isAllRequiredTermsAgreed by termsViewModel.isAllRequiredTermsAgreed.collectAsStateWithLifecycle()

            TermsData(
                termList = termList,
                termDetails = termDetails,
                isAllRequiredTermsAgreed = isAllRequiredTermsAgreed
            )
        }

        WelcomeScreen(
            navController = navController,
            welcomeArgument = welcomeArgument,
            welcomeData = welcomeData,
            termsArgument = termsArgument,
            termsData = termsData
        )
    }
}