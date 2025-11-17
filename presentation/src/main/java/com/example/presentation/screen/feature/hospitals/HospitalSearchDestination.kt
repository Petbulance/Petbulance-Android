package com.example.presentation.screen.feature.hospitals

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.presentation.utils.nav.ScreenDestinations

fun NavGraphBuilder.hospitalSearchDestination(navController: NavController) {
    composable(
        route = ScreenDestinations.HospitalSearch.route
    ) {
        val viewModel: HospitalSearchViewModel = hiltViewModel()

        val argument: HospitalSearchArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            HospitalSearchArgument(
                state = state,
                intent = viewModel::onIntent,
                event = viewModel.eventFlow
            )
        }

        val data: HospitalSearchData = let {
            val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
            val hospitalsResult by viewModel.hospitalsResult.collectAsStateWithLifecycle()
            val searchHospitalParams by viewModel.searchHospitalParams.collectAsStateWithLifecycle()
            val currentSelectedHospitalId by viewModel.currentSelectedHospitalId.collectAsStateWithLifecycle()
            val cameraPosition by viewModel.cameraPosition.collectAsStateWithLifecycle()
            val isLastPage by viewModel.isLastPage.collectAsStateWithLifecycle()

            HospitalSearchData(
                currentLocation = currentLocation,
                hospitalsResult = hospitalsResult,
                searchHospitalParams = searchHospitalParams,
                cameraPosition = cameraPosition,
                currentSelectedHospitalId = currentSelectedHospitalId,
                isLastPage = isLastPage
            )
        }

        HospitalSearchScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}