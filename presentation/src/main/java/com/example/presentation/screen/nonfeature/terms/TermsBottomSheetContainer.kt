package com.example.presentation.screen.nonfeature.terms

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsBottomSheetContainer(
    onAgreementSuccess: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val viewModel: TermsViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val termList by viewModel.termList.collectAsStateWithLifecycle()
    val termDetails by viewModel.termDetails.collectAsStateWithLifecycle()

    val argument = TermsArgument(
        state = state,
        intent = viewModel::onIntent,
        event = viewModel.eventFlow
    )

    val data = TermsData(
        termList = termList,
        termDetails = termDetails
    )

    LaunchedEffect(viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TermsEvent.Agreement.Success -> {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        onAgreementSuccess()
                    }
                }
                else -> {}
            }
        }
    }

    TermsBottomSheet(
        argument = argument,
        data = data,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    )
}