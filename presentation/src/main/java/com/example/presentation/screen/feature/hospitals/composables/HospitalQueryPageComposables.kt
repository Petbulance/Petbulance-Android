package com.example.presentation.screen.feature.hospitals.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXXS

@Composable
fun HospitalQueryPage(
    recentQueries: List<String>,
    currentDistrict: String?,
    currentAnimalSpecies: List<AnimalSpecies>?,
    recentViewedHospitals: List<Hospital>,
    onRecentQueryClicked: (String) -> Unit,
    onRecentViewedHospitalClicked: (Hospital) -> Unit,
    onRegionChipClicked: () -> Unit,
    onSpeciesChipClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(spacingMedium)
        ) {
            BasicChip(
                text = currentDistrict ?: "지역",
                showLeadingIcon = false,
                showTrailingIcon = true,
                onClick = onRegionChipClicked
            )

            val text = if (currentAnimalSpecies == null || currentAnimalSpecies.isEmpty()) "동물종"
            else currentAnimalSpecies.joinToString { it.toKorean() }

            BasicChip(
                text = text,
                showLeadingIcon = false,
                showTrailingIcon = true,
                onClick = onSpeciesChipClicked
            )
        }
        RecentQueryContents(
            title = "최근 검색어",
            contents = recentQueries,
            onElementClicked = onRecentQueryClicked
        )
        RecentQueryContents(
            title = "최근 본 병원",
            contents = recentViewedHospitals,
            onElementClicked = onRecentViewedHospitalClicked
        )
    }
}

@Composable
fun <T> RecentQueryContents(
    title: String,
    contents: List<T>,
    onElementClicked: (T) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = spacingXS, bottom = spacingMedium)
            .padding(horizontal = spacingMedium)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.emp(),
            color = colorScheme.text.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(spacingXS),
        ) {
            contents.forEach { content ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = colorScheme.bg.frame.medium,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = spacingXS, vertical = spacingXXXS)
                        .clickable { onElementClicked(content) }
                ) {
                    Text(
                        text = content.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.text.secondary
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Clear),
                        contentDescription = "clear",
                        size = iconSizeMs,
                        tint = colorScheme.icon.basic
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun RecentQueryContentsPreview() {
    PetbulanceTheme {
        RecentQueryContents(
            "최근 본 항목",
            listOf("서울", "연세", "고려"),
            {}
        )
    }
}
