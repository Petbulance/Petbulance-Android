package com.example.presentation.component.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicDialog

@Composable
fun LoginRequiredDialog(
    onLoginClicked: () -> Unit,
    onSkipClicked: () -> Unit
){
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
                    text = "Sample Text",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PetbulanceTheme.colorScheme.text.caption
                )
                Text(
                    text = "Sample Text",
                    style = MaterialTheme.typography.headlineSmall,
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