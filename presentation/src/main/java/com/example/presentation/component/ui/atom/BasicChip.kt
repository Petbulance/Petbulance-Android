package com.example.presentation.component.ui.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.RoundedCorner
import com.example.presentation.component.ui.dropShadow
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme as pbColor

@Composable
fun BasicChip(
    text: String,
    modifier: Modifier = Modifier,
    showLeadingIcon: Boolean = false,
    showTrailingIcon: Boolean = false,
    borderColor: Color = colorScheme.border.active,
    backgroundColor: Color = colorScheme.bg.frame.default,
    textColor: Color = colorScheme.text.primary,
    dropShadow: Boolean = false,
    onClick: () -> Unit
) {
    val baseModifier = if (dropShadow) {
        modifier.dropShadow(
            shape = RoundedCorner,
            color = borderColor.copy(alpha = 0.25f),
            blur = 6.dp,
            offsetX = 0.dp,
            offsetY = 0.dp,
            spread = 2.dp
        ) /* TODO : 명세대로 하면 그림자가 너무 안보여서, 조금 수정함 */
    } else {
        modifier
    }

    Box(
        modifier = baseModifier
            .background(color = backgroundColor, shape = RoundedCorner)
            .border(BorderStroke(1.dp, borderColor), RoundedCorner)
            .clip(RoundedCorner)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = spacingSmall, vertical = 6.dp)
        ) {
            if (showLeadingIcon) {
                BasicIcon(
                    iconResource = IconResource.Drawable(R.drawable.icon_sort_desc),
                    contentDescription = null,
                    size = 18.dp,
                    tint = pbColor.icon.dark
                )
                Spacer(modifier = Modifier.width(spacingXXS))
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.emp(),
                color = textColor,
                maxLines = 1
            )

            if (showTrailingIcon) {
                Spacer(modifier = Modifier.width(spacingXXS))
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Outlined.KeyboardArrowDown),
                    contentDescription = null,
                    size = 18.dp,
                    tint = pbColor.icon.dark
                )
            }
        }
    }
}


@Preview(apiLevel = 34)
@Composable
private fun BasicChipPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            BasicChip(
                text = "동물종",
                showLeadingIcon = false,
                showTrailingIcon = true,
                onClick = { }
            )

            BasicChip(
                text = "리뷰 많은 순",
                showLeadingIcon = true,
                showTrailingIcon = false,
                onClick = { }
            )

            BasicChip(
                text = "진료중",
                showLeadingIcon = false,
                showTrailingIcon = false,
                borderColor = colorScheme.tag.blue.medium,
                textColor = colorScheme.tag.blue.medium,
                dropShadow = true,
                onClick = { }
            )

            BasicChip(
                text = "진료중",
                showLeadingIcon = false,
                showTrailingIcon = false,
                borderColor = colorScheme.text.tertiary,
                textColor = colorScheme.text.tertiary,
                dropShadow = true,
                onClick = { }
            )
        }

    }
}