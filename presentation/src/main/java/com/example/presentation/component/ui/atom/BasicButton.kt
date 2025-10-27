package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.DefaultRoundedCorner

@Composable
fun BasicButton(
    text: String,
    type: ButtonType,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (type) {
        ButtonType.PRIMARY -> PetbulanceTheme.colorScheme.action.primary.default
        ButtonType.SECONDARY -> PetbulanceTheme.colorScheme.action.primary.disabled
        ButtonType.DEFAULT -> PetbulanceTheme.colorScheme.action.primary.disabled
    }

    val textColor = when (type) {
        ButtonType.PRIMARY -> PetbulanceTheme.colorScheme.text.inverse
        ButtonType.SECONDARY -> PetbulanceTheme.colorScheme.text.caption
        ButtonType.DEFAULT -> PetbulanceTheme.colorScheme.text.caption
    }

    Box(
        modifier = modifier
            .clickable { onClicked() }
            .background(buttonColor, DefaultRoundedCorner)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge.emp(),
            modifier = Modifier.padding(10.dp)
        )
    }
}

enum class ButtonType {
    PRIMARY, SECONDARY, DEFAULT
}

@Preview
@Composable
private fun AppButtonPreview() {
    PetbulanceTheme {
        BasicButton(
            text = "Button",
            onClicked = {},
            type = ButtonType.PRIMARY
        )
    }
}