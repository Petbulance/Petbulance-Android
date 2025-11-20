package com.example.presentation.screen.feature.hospitals

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.model.type.Region
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Space16
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalFilterBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    intent: (HospitalFilterIntent) -> Unit,
    initialTab: FilterTab,
    selectedRegion: String?,
    selectedDistrict: String?,
    selectedAnimalSpecies: List<AnimalSpecies>?,
    onApplyFilter: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val regionsMap = remember { Region.entries.associate { it.displayName to it.districts } }
    val animalSpecies = remember { AnimalSpecies.entries }

    var currentTab by remember { mutableStateOf(initialTab) }
    var currentSelectedRegion by remember {
        mutableStateOf(
            selectedRegion ?: regionsMap.keys.first()
        )
    }
    var currentSelectedDistrict by remember { mutableStateOf(selectedDistrict) }
    var currentSelectedAnimalSpecies by remember { mutableStateOf(selectedAnimalSpecies) }

    val resetFilters = {
        currentSelectedRegion = regionsMap.keys.first()
        currentSelectedDistrict = null
        currentSelectedAnimalSpecies = emptyList()
        intent(HospitalFilterIntent.OnFilterReset)
    }

    BackHandler {
        resetFilters()
        onDismissRequest()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            resetFilters()
            onDismissRequest()
        },
        containerColor = Color.White
    ) {
        HospitalBottomSheetContents(
            currentTab = currentTab,
            onSelectedTabChanged = { currentTab = it },
            regionsMap = regionsMap,
            animalSpecies = animalSpecies,
            selectedRegion = currentSelectedRegion,
            selectedDistrict = currentSelectedDistrict,
            selectedAnimalSpecies = currentSelectedAnimalSpecies,
            onRegionSelected = { region, district ->
                currentSelectedRegion = region
                currentSelectedDistrict = district
            },
            onSpeciesSelected = { species ->
                currentSelectedAnimalSpecies = species
            },
            resetFilters = resetFilters,
            onApplyFilter = onApplyFilter
        )
    }
}

@Composable
private fun HospitalBottomSheetContents(
    currentTab: FilterTab,
    onSelectedTabChanged: (FilterTab) -> Unit,
    regionsMap: Map<String, List<String>>,
    animalSpecies: List<AnimalSpecies>,
    selectedRegion: String,
    selectedDistrict: String?,
    selectedAnimalSpecies: List<AnimalSpecies>?,
    onRegionSelected: (String, String?) -> Unit,
    onSpeciesSelected: (List<AnimalSpecies>) -> Unit,
    resetFilters: () -> Unit,
    onApplyFilter: () -> Unit
) {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        TabRow(
            selectedTabIndex = currentTab.ordinal,
            modifier = Modifier
                .padding(horizontal = spacingMedium)
                .padding(end = 160.dp),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentTab.ordinal]),
                    height = 3.dp,
                    color = colorScheme.border.active
                )
            },
            divider = {},
            containerColor = colorScheme.border.white
        ) {
            Tab(
                selected = currentTab == FilterTab.REGION,
                onClick = { onSelectedTabChanged(FilterTab.REGION) },
                text = {
                    Text(
                        "지역",
                        color = if (currentTab == FilterTab.REGION) colorScheme.text.primary
                        else colorScheme.text.disabled,
                        style = MaterialTheme.typography.bodySmall.emp()
                    )
                },
                modifier = Modifier.weight(1f)
            )
            Tab(
                selected = currentTab == FilterTab.ANIMAL_SPECIES,
                onClick = { onSelectedTabChanged(FilterTab.ANIMAL_SPECIES) },
                text = {
                    Text(
                        "동물종",
                        color = if (currentTab == FilterTab.ANIMAL_SPECIES) colorScheme.text.primary
                        else colorScheme.text.disabled,
                        style = MaterialTheme.typography.bodySmall.emp()
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorScheme.border.verySubtle
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingLarge),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier.clickable { resetFilters() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacingXXS)
            ) {
                Text(
                    text = "선택 초기화",
                    style = MaterialTheme.typography.labelLarge,
                    color = colorScheme.text.tertiary,
                    modifier = Modifier.padding(vertical = spacingXS)
                )
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.Refresh),
                    contentDescription = "reset",
                    tint = colorScheme.icon.medium
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorScheme.border.verySubtle
        )

        Box(modifier = Modifier.heightIn(min = 300.dp, max = 400.dp)) {
            when (currentTab) {
                FilterTab.REGION -> {
                    RegionSelection(
                        regions = regionsMap,
                        selectedRegion = selectedRegion,
                        selectedDistrict = selectedDistrict,
                        onRegionSelected = { region, district ->
                            onRegionSelected(region, district)
                        }
                    )
                }

                FilterTab.ANIMAL_SPECIES -> {
                    AnimalSpeciesSelection(
                        animalSpecies = animalSpecies,
                        selectedAnimalSpecies = selectedAnimalSpecies,
                        onSpeciesSelected = { species ->
                            val newList =
                                selectedAnimalSpecies?.toMutableList() ?: mutableListOf()
                            if (newList.contains(species)) newList.remove(species)
                            else newList.add(species)
                            onSpeciesSelected(newList)
                        }
                    )
                }
            }
        }

        Space16()
    }
}

@Composable
private fun RegionSelection(
    regions: Map<String, List<String>>,
    selectedRegion: String,
    selectedDistrict: String?,
    onRegionSelected: (String, String?) -> Unit
) {
    val borderColor = colorScheme.border.verySubtle
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        LazyColumn {
            items(regions.keys.toList()) { region ->
                Box(
                    modifier = Modifier
                        .background(
                            if (selectedRegion == region) colorScheme.bg.frame.default
                            else colorScheme.bg.frame.subtle
                        )
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                ) {
                    Text(
                        text = region,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedRegion == region) colorScheme.text.primary
                        else colorScheme.text.caption,
                        modifier = Modifier
                            .clickable {
                                onRegionSelected(
                                    region,
                                    regions[selectedRegion]!!.first()
                                )
                            }
                            .padding(horizontal = spacingLarge, vertical = spacingSmall)
                            .align(Alignment.Center),
                        fontWeight = if (selectedRegion == region) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(start = spacingSmall)
                .fillMaxWidth()
        ) {
            items(regions[selectedRegion] ?: emptyList()) { district ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        }
                ) {
                    Text(
                        text = district,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clickable { onRegionSelected(selectedRegion, district) }
                            .background(
                                color = if (selectedDistrict == district) colorScheme.bg.frame.subtle
                                else Color.Transparent
                            )
                            .padding(horizontal = spacingLarge, vertical = spacingSmall),
                        fontWeight = if (selectedDistrict == district) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedDistrict == district) colorScheme.action.primary.default
                        else colorScheme.text.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimalSpeciesSelection(
    animalSpecies: List<AnimalSpecies>,
    selectedAnimalSpecies: List<AnimalSpecies>?,
    onSpeciesSelected: (AnimalSpecies) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        animalSpecies.forEach { species ->
            val isSelected = selectedAnimalSpecies?.contains(species) ?: false
            val color = if (isSelected) colorScheme.tag.blue.medium else colorScheme.text.tertiary
            BasicChip(
                text = species.toKorean(),
                onClick = { onSpeciesSelected(species) },
                borderColor = color,
                textColor = color,
                dropShadow = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun HospitalFilterBottomSheetPreview() {
    val regionsMap = remember { Region.entries.associate { it.displayName to it.districts } }

    PetbulanceTheme {
        HospitalBottomSheetContents(
            currentTab = FilterTab.REGION,
            onSelectedTabChanged = {},
            regionsMap = regionsMap,
            animalSpecies = AnimalSpecies.entries,
            selectedRegion = "서울시",
            selectedDistrict = null,
            selectedAnimalSpecies = null,
            onRegionSelected = { _, _ -> },
            onSpeciesSelected = {},
            resetFilters = {},
            onApplyFilter = {}

        )
    }
}
