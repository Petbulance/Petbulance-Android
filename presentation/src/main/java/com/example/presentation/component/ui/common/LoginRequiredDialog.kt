package com.example.presentation.component.ui.common

import android.Manifest.permission_group.PHONE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicDialog

@Composable
fun LoginRequiredDialog(
    onLoginClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    BasicDialog {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "알림",
                    style = MaterialTheme.typography.bodyMedium.emp(),
                    color = PetbulanceTheme.colorScheme.text.caption
                )
                Text(
                    text = "로그인이 필요한 서비스에요.\n로그인하시겠어요?",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = PetbulanceTheme.colorScheme.text.primary
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "그냥 볼래요",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = onSkipClicked,
                    modifier = Modifier.weight(1f)
                )
                BasicButton(
                    text = "로그인",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 28.dp,
                    onClicked = onLoginClicked,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(name = "Pixel 9", device = Devices.PIXEL_9)
@Preview(name = "Pixel 7 Pro (Wide)", device = Devices.PIXEL_7_PRO)
@Preview(name = "Pixel 5 (Normal)", device = Devices.PIXEL_5)
@Preview(name = "Pixel 3 (Small)", device = Devices.PIXEL_3)
@Preview(name = "Default", device = PHONE)
@Composable
private fun LoginRequiredDialogPreview() {
    PetbulanceTheme {
        LoginRequiredDialog({}, {})
    }
}