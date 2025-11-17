package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.HospitalSortOption
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingXL


@Composable
fun SortOptionDialog(
    currentSortOption: HospitalSortOption,
    onDismissRequest: () -> Unit,
    onSortOptionChanged: (HospitalSortOption) -> Unit
) {
    BasicDialog(
        position = Alignment.BottomCenter,
        backHandler = onDismissRequest,
        modifier = Modifier.padding(bottom = 64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXL),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "어떤 순서로 정렬할까요?",
                    style = MaterialTheme.typography.titleMedium.emp(),
                    color = PetbulanceTheme.colorScheme.text.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SortOptionRow(
                        text = "리뷰 많은 순",
                        isChecked = (currentSortOption == HospitalSortOption.REVIEWS),
                        onClicked = { onSortOptionChanged(HospitalSortOption.REVIEWS) }
                    )
                    SortOptionRow(
                        text = "가까운 순",
                        isChecked = (currentSortOption == HospitalSortOption.DISTANCE),
                        onClicked = { onSortOptionChanged(HospitalSortOption.DISTANCE) }
                    )
                    SortOptionRow(
                        text = "평점 높은 순",
                        isChecked = (currentSortOption == HospitalSortOption.RATINGS),
                        onClicked = { onSortOptionChanged(HospitalSortOption.RATINGS) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SortOptionRow(
    text: String,
    isChecked: Boolean,
    onClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.emp(),
            color = PetbulanceTheme.colorScheme.text.tertiary,
        )
        BasicIcon(
            iconResource = IconResource.Drawable(R.drawable.icon_checkmark),
            size = 20.dp,
            contentDescription = "checkmark",
            tint = if (isChecked) PetbulanceTheme.colorScheme.status.info.default
            else PetbulanceTheme.colorScheme.icon.disabled /* TODO : 색상 수정 */
        )
    }
}


@Preview(apiLevel = 34)
@Composable
private fun SortOptionDialogPreview() {
    PetbulanceTheme {
        SortOptionDialog(
            currentSortOption = HospitalSortOption.RATINGS,
            onDismissRequest = {},
            onSortOptionChanged = {}
        )
    }
}