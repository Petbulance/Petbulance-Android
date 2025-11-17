package com.example.presentation.component.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.RoundedCorner
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS

enum class BasicFabType {
    PRIMARY, SECONDARY
}

@Composable
fun BasicFab(
    modifier: Modifier = Modifier,
    text: String,
    leadingIcon: IconResource?,
    type: BasicFabType = BasicFabType.PRIMARY,
    onClick: () -> Unit
) {
    val contentColor = when (type) {
        BasicFabType.PRIMARY -> colorScheme.text.inverse
        BasicFabType.SECONDARY -> colorScheme.text.secondary
    }

    val backgroundColor = when (type) {
        BasicFabType.PRIMARY -> colorScheme.action.primary.default
        BasicFabType.SECONDARY -> colorScheme.bg.frame.default
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(backgroundColor, RoundedCorner)
            .border(width = 1.dp, color = colorScheme.action.primary.default, shape = RoundedCorner)
            .padding(vertical = spacingXS, horizontal = spacingSmall)
            .clickable{ onClick() }
    ) {
        if (leadingIcon != null) {
            BasicIcon(
                iconResource = leadingIcon,
                contentDescription = "leadingIcon",
                size = iconSizeMs,
                tint = contentColor
            )
        }

        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodySmall
        )
    }

}

@Preview(apiLevel = 34)
@Composable
private fun BasicFabPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicFab(
                text = "목록보기",
                leadingIcon = IconResource.Vector(Icons.AutoMirrored.Filled.List),
                type = BasicFabType.PRIMARY,
                onClick = {}
            )
            BasicFab(
                text = "목록보기",
                leadingIcon = IconResource.Vector(Icons.AutoMirrored.Filled.List),
                type = BasicFabType.SECONDARY,
                onClick = {}
            )
        }

    }
}