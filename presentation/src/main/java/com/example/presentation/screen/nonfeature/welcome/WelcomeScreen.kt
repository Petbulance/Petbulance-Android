package com.example.presentation.screen.nonfeature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Space16
import com.example.presentation.component.ui.atom.DefaultButton
import com.example.presentation.component.ui.atom.DefaultButtonType
import com.example.presentation.screen.nonfeature.terms.TermsArgument
import com.example.presentation.screen.nonfeature.terms.TermsBottomSheet
import com.example.presentation.screen.nonfeature.terms.TermsData
import com.example.presentation.screen.nonfeature.terms.TermsState
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import com.example.presentation.utils.nav.safePopBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    welcomeArgument: WelcomeArgument,
    welcomeData: WelcomeData,
    termsArgument: TermsArgument,
    termsData: TermsData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val tempNickname = welcomeData.tempNickname
    val isAllRequiredTermsAgreed = termsData.isAllRequiredTermsAgreed

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(!isAllRequiredTermsAgreed) }

    LaunchedEffect(welcomeArgument.event) {
        welcomeArgument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    LaunchedEffect(termsArgument.event) {
        termsArgument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            WelcomeScreenContents(
                tempNickname = tempNickname,
                isAllRequiredTermsAgreed = isAllRequiredTermsAgreed,
                onTermsBottomSheetCalled = {
                    showBottomSheet = true
                },
                onMoveToHomeScreen = {
                    navController.safeNavigate(ScreenDestinations.Home.route)
                }
            )
        }
    }

    if (showBottomSheet) {
        TermsBottomSheet(
            argument = termsArgument,
            data = termsData,
            onDismissRequest = {
                showBottomSheet = false
                navController.safePopBackStack()
                coroutineScope.launch { sheetState.hide() }
            },
            sheetState = sheetState
        )
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
private fun WelcomeScreenContents(
    tempNickname: String,
    isAllRequiredTermsAgreed: Boolean,
    onTermsBottomSheetCalled: () -> Unit,
    onMoveToHomeScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Space16()

        TextContents(tempNickname)

        if(isAllRequiredTermsAgreed){
            DefaultButton(
                text = "시작하기",
                type = DefaultButtonType.PRIMARY,
                onClicked = {
                    onMoveToHomeScreen()
                },
            )
        }
        else {
            DefaultButton(
                text = "약관 동의하기",
                type = DefaultButtonType.PRIMARY,
                onClicked = { onTermsBottomSheetCalled() },
            )
        }
    }
}

@Composable
private fun TextContents(tempNickname: String){
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.welcome),
            contentDescription = "Welcome Image",
            modifier = Modifier.size(90.dp).padding(bottom = 16.dp)
        )

        val text1 = buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = PetbulanceTheme.colorScheme.action.primary.default)
            ) {
                append(tempNickname)
            }
            withStyle(
                style = SpanStyle(color = PetbulanceTheme.colorScheme.text.secondary)
            ) {
                append("님, 환영해요!")
            }
        }

        val text2 = buildAnnotatedString {
            withStyle(
                style = SpanStyle(color = PetbulanceTheme.colorScheme.action.primary.default)
            ) {
                append("펫뷸런스")
            }
            withStyle(
                style = SpanStyle(color = PetbulanceTheme.colorScheme.text.secondary)
            ) {
                append("에서 지금 필요한\n병원 정보를 검색하세요")
            }
        }

        Text(
            text = text1,
            style = MaterialTheme.typography.titleMedium.emp()
        )
        Text(
            text = text2,
            style = MaterialTheme.typography.bodyMedium.emp(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun WelcomeScreenPreview() {
    PetbulanceTheme {
        WelcomeScreen(
            navController = rememberNavController(),
            welcomeArgument = WelcomeArgument(
                intent = { },
                state = WelcomeState.Init,
                event = MutableSharedFlow()
            ),
            welcomeData = WelcomeData.empty(),
            termsArgument = TermsArgument(
                state = TermsState.Init,
                intent = { },
                event = MutableSharedFlow()
            ),
            termsData = TermsData.empty()
        )
    }
}