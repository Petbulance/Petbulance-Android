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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.DefaultRoundedCorner

@Composable
fun DefaultButton(
    text: String,
    type: DefaultButtonType,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = when (type) {
        DefaultButtonType.PRIMARY -> PetbulanceTheme.colorScheme.action.primary.default
        DefaultButtonType.SECONDARY -> PetbulanceTheme.colorScheme.action.primary.disabled
        DefaultButtonType.DEFAULT -> Color.Transparent
    }

    val textColor = when (type) {
        DefaultButtonType.PRIMARY -> PetbulanceTheme.colorScheme.text.inverse
        DefaultButtonType.SECONDARY -> PetbulanceTheme.colorScheme.text.disabled
        DefaultButtonType.DEFAULT -> PetbulanceTheme.colorScheme.text.caption
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
            style = MaterialTheme.typography.titleMedium.emp(),
            modifier = Modifier.padding(16.dp)
        )
    }
}

enum class DefaultButtonType {
    PRIMARY, SECONDARY, DEFAULT
}

@Preview
@Composable
private fun AppButtonPreview() {
    PetbulanceTheme {
        DefaultButton(
            text = "Button",
            onClicked = {},
            type = DefaultButtonType.PRIMARY
        )
    }
}