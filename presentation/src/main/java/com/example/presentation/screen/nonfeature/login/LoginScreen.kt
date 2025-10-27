package com.example.presentation.screen.nonfeature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.nonfeature.login.LoginProviderType
import com.example.presentation.R
import com.example.presentation.component.theme.ColorObject
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.DefaultRoundedCorner
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.example.presentation.utils.hooks.login.rememberGoogleLoginManager
import com.example.presentation.utils.hooks.login.rememberKakaoLoginManager
import com.example.presentation.utils.nav.ScreenDestinations
import com.example.presentation.utils.nav.safeNavigate
import kotlinx.coroutines.flow.MutableSharedFlow
import rememberNaverLoginManager

@Composable
fun LoginScreen(
    navController: NavController,
    argument: LoginArgument,
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val launchGoogleLogin = rememberGoogleLoginManager { accessToken ->
        argument.intent(LoginIntent.Login(accessToken ?: "", LoginProviderType.GOOGLE))
    }

    val launchNaverLogin = rememberNaverLoginManager { accessToken ->
        argument.intent(LoginIntent.Login(accessToken ?: "", LoginProviderType.NAVER))
    }

    val launchKakaoLogin = rememberKakaoLoginManager { accessToken ->
        argument.intent(LoginIntent.Login(accessToken ?: "", LoginProviderType.KAKAO))
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is LoginEvent.Login.Success -> {
                    /* TODO : navigate to next screen */
                }

                else -> {}
            }
        }
    }


    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LoginScreenContents(
                onGoogleLoginButtonClicked = launchGoogleLogin,
                onKakaoLoginButtonClicked = launchKakaoLogin,
                onNaverLoginButtonClicked = launchNaverLogin
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
}

@Composable
private fun LoginScreenContents(
    onGoogleLoginButtonClicked: () -> Unit,
    onKakaoLoginButtonClicked: () -> Unit,
    onNaverLoginButtonClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(18.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painterResource(R.drawable.petbulance_logo_login),
                contentDescription = "Petbulance Logo"
            )

            LoginScreenTextSection()

            Spacer(modifier = Modifier.height(64.dp))

            LoginButtonColumn(
                onGoogleLoginButtonClicked = onGoogleLoginButtonClicked,
                onKakaoLoginButtonClicked = onKakaoLoginButtonClicked,
                onNaverLoginButtonClicked = onNaverLoginButtonClicked
            )
        }
    }
}

@Composable
private fun LoginScreenTextSection() {
    val text = "검증된 특수동물병원 정보부터\n집사들의 실생활 꿀팁까지,"
    val text2 = buildAnnotatedString {
        append("지금 ")

        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = PetbulanceTheme.colorScheme.action.primary.default
            )
        ) {
            append("펫뷸런스")
        }

        append("에서 시작하세요!")
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.emp(),
        textAlign = TextAlign.Center
    )
    Text(
        text = text2,
        style = MaterialTheme.typography.bodyMedium.emp()
    )
}

@Composable
private fun LoginButtonColumn(
    onGoogleLoginButtonClicked: () -> Unit,
    onKakaoLoginButtonClicked: () -> Unit,
    onNaverLoginButtonClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginButton(
            provider = LoginProviderType.KAKAO,
            handler = onKakaoLoginButtonClicked
        )
        LoginButton(
            provider = LoginProviderType.NAVER,
            handler = onNaverLoginButtonClicked
        )
        LoginButton(
            provider = LoginProviderType.GOOGLE,
            iconSize = 28.dp,
            handler = onGoogleLoginButtonClicked
        )
    }
}

@Composable
private fun LoginButton(provider: LoginProviderType, iconSize: Dp = 20.dp, handler: () -> Unit) {

    val iconRes = when (provider) {
        LoginProviderType.GOOGLE -> R.drawable.login_google
        LoginProviderType.KAKAO -> R.drawable.login_kakao
        LoginProviderType.NAVER -> R.drawable.login_naver
    }

    val providerText = when (provider) {
        LoginProviderType.GOOGLE -> "구글"
        LoginProviderType.KAKAO -> "카카오"
        LoginProviderType.NAVER -> "네이버"
    }

    val textColor = when (provider) {
        LoginProviderType.GOOGLE -> Color.Black
        LoginProviderType.KAKAO -> Color.Black
        LoginProviderType.NAVER -> Color.White
    }

    val backgroundColor = when (provider) {
        LoginProviderType.GOOGLE -> ColorObject.Provider.GOOGLE
        LoginProviderType.KAKAO -> ColorObject.Provider.KAKAO
        LoginProviderType.NAVER -> ColorObject.Provider.NAVER
    }

    val googleButtonBorder =
        if (provider == LoginProviderType.GOOGLE) Color(0xFFD7D7D7) else Color.Transparent

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(width = 1.dp, color = googleButtonBorder, shape = DefaultRoundedCorner)
            .background(color = backgroundColor, shape = DefaultRoundedCorner)
            .clickable { handler() }
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(iconRes),
            contentDescription = "Log in with $providerText"
        )
        Text(
            text = providerText + "로 시작하기",
            color = textColor,
            style = MaterialTheme.typography.bodyLarge.emp(),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun LoginScreenPreview() {
    PetbulanceTheme {
        LoginScreen(
            navController = rememberNavController(),
            argument = LoginArgument(
                intent = { },
                state = LoginState.Init,
                event = MutableSharedFlow()
            )
        )
    }
}