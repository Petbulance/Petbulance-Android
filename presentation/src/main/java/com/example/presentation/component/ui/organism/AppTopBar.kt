package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.spacingXS

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    topBarInfo: TopBarInfo,
    background: Color = colorScheme.bg.frame.default,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                clip = false
            )
            .background(color = background)
    ) {
        if (topBarInfo.isLeadingIconAvailable) {
            TopBarIcon(
                iconResource = topBarInfo.leadingIconResource,
                contentDescription = "Leading icon",
                onIconClicked = topBarInfo.onLeadingIconClicked,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }

        Text(
            text = topBarInfo.text,
            color = colorScheme.text.primary,
            style = MaterialTheme.typography.titleMedium.emp(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(
                    when (topBarInfo.textAlignment) {
                        TopBarAlignment.CENTER -> Alignment.Center
                        TopBarAlignment.START -> Alignment.CenterStart
                    }
                )
                .padding(
                    start = when (topBarInfo.textAlignment) {
                        TopBarAlignment.CENTER -> 56.dp
                        TopBarAlignment.START -> 16.dp
                    },
                    end = 56.dp * topBarInfo.trailingIcons.size + 16.dp,
                    top = spacingXS,
                    bottom = spacingXS
                )
                .padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            topBarInfo.trailingIcons.forEach { (icon, onClick) ->
                TopBarIcon(
                    iconResource = icon,
                    contentDescription = "trailing icon : $icon",
                    onIconClicked = onClick
                )
            }
        }
    }
}

@Composable
private fun TopBarIcon(
    iconResource: IconResource,
    contentDescription: String,
    onIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicIcon(
        iconResource = iconResource,
        contentDescription = contentDescription,
        size = 20.dp,
        tint = PetbulanceTheme.colorScheme.icon.dark,
        modifier = modifier.clickable { onIconClicked() }
    )
}

enum class TopBarAlignment {
    START, CENTER
}

data class TopBarInfo(
    val text: String,
    val textAlignment: TopBarAlignment = TopBarAlignment.CENTER,
    val isLeadingIconAvailable: Boolean = false,
    val onLeadingIconClicked: () -> Unit = {},
    val leadingIconResource: IconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
    val trailingIcons: List<Pair<IconResource, () -> Unit>> = emptyList(),
)

@Preview(apiLevel = 34)
@Composable
private fun AppTopBarPreview() {
    PetbulanceTheme {
        val textList = listOf(
            "Short example",
            "LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG Example",
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            textList.forEach { text ->
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.CENTER,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.CENTER,
                        isLeadingIconAvailable = false,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )

            }
        }
    }
}