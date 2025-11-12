package com.example.presentation.screen.nonfeature.splash

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun SplashScreen(
    navController: NavController,
    argument: SplashArgument,
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is SplashEvent.Navigate.ToLogin -> {
                    navController.safeNavigate(ScreenDestinations.Login.route) {
                        popUpTo(ScreenDestinations.Splash.route) {
                            inclusive = true
                        }
                    }
                }

                is SplashEvent.Navigate.ToHome -> {
                    Log.d("siria22", "ToHome")
                    /* TODO : navigate to home screen */
                }

                is SplashEvent.Navigate.ToTermsAgreement -> {
                    Log.d("siria22", "TermsUpdateNeeded")
                    /* TODO : navigate to terms agreement screen */
                }

                is SplashEvent.DataFetch.Error -> {
                    errorDialogState = ErrorDialogState.fromErrorEvent(event)
                }
            }
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SplashScreenContents(

            )
        }
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
                navController.safeNavigate(ScreenDestinations.Home.route)
            }
        )
    }

    // BackHandler { }
}

@Composable
private fun SplashScreenContents(

) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text("text")

    }
}


@Preview(apiLevel = 34)
@Composable
private fun SplashScreenPreview() {
    PetbulanceTheme {
        SplashScreen(
            navController = rememberNavController(),
            argument = SplashArgument(
                intent = { },
                state = SplashState.Init,
                event = MutableSharedFlow()
            )
        )
    }
}