package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme

enum class BasicButtonSize {
    XS, S, M, L
}

enum class BasicButtonType {
    PRIMARY, SECONDARY
}

enum class BasicButtonRoundType {
    SQUARE, ROUNDED
}

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    text: String,
    size: BasicButtonSize,
    buttonType: BasicButtonType = BasicButtonType.PRIMARY,
    radius: Dp = 8.dp,
    onClicked: () -> Unit
) {
    val horizontalPadding = when (size) {
        BasicButtonSize.XS -> 8.dp
        BasicButtonSize.S -> 12.dp
        BasicButtonSize.M -> 16.dp
        BasicButtonSize.L -> 20.dp
    }

    val minHeight = when (size) {
        BasicButtonSize.XS -> 28.dp
        BasicButtonSize.S -> 32.dp
        BasicButtonSize.M -> 44.dp
        BasicButtonSize.L -> 52.dp
    }

    val textStyle = when (size) {
        BasicButtonSize.XS -> MaterialTheme.typography.labelMedium.copy(fontWeight = W500)
        BasicButtonSize.S -> MaterialTheme.typography.bodySmall.copy(fontWeight = W500)
        BasicButtonSize.M -> MaterialTheme.typography.bodyLarge.copy(fontWeight = W500)
        BasicButtonSize.L -> MaterialTheme.typography.titleMedium.copy(fontWeight = W500)
    }

    val background = when (buttonType) {
        BasicButtonType.PRIMARY -> PetbulanceTheme.colorScheme.action.primary.default
        BasicButtonType.SECONDARY -> PetbulanceTheme.colorScheme.bg.frame.default
    }

    val textColor = when (buttonType) {
        BasicButtonType.PRIMARY -> PetbulanceTheme.colorScheme.text.inverse
        BasicButtonType.SECONDARY -> PetbulanceTheme.colorScheme.action.primary.default
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(minHeight)
            .background(background, RoundedCornerShape(radius))
            .border(
                width = 1.dp, color = PetbulanceTheme.colorScheme.action.primary.default,
                shape = RoundedCornerShape(radius)
            )
            .padding(
                horizontal = horizontalPadding,
                vertical = 4.dp
            )
            .clickable { onClicked() }
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun BasicSecondarySquareButtonPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicButtonSize.entries.forEach { size ->
                BasicButton(
                    text = "프로필 수정",
                    size = size,
                    onClicked = {},
                )
            }
        }
    }
}