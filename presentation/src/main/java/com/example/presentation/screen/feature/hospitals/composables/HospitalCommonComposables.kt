package com.example.presentation.screen.feature.hospitals.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.spacingLarge

@Composable
fun FineLocationPermissionRequestDialog(
    onShowTermLinkClicked: () -> Unit,
    onAgreeButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    BasicDialog {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "병원 검색을 이용하려면\n위치정보 이용 약관 동의가 필요해요",
                    style = MaterialTheme.typography.titleSmall,
                    color = colorScheme.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "(선택)위치 정보 이용 약관",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.text.caption
                    )
                    Text(
                        text = "약관보기",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.action.link.default,
                        modifier = Modifier.clickable { onShowTermLinkClicked() }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "다음에",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = { onDismissButtonClicked() },
                    modifier = Modifier.weight(1f)
                )
                BasicButton(
                    text = "동의하기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 28.dp,
                    onClicked = { onAgreeButtonClicked() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

}


@Preview(apiLevel = 34)
@Composable
private fun FineLocationPermissionRequestDialogPreview() {
    PetbulanceTheme {
        FineLocationPermissionRequestDialog({}, {}, {})
    }
}
