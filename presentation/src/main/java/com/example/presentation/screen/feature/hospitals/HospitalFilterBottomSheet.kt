package com.example.presentation.screen.feature.hospitals

import android.Manifest.permission_group.PHONE
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.outlined.LocationSearching
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.model.type.Region
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeLarge
import com.example.presentation.component.ui.iconSizeMedium
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXL
import com.example.presentation.component.ui.spacingXXS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalFilterBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    initialTab: FilterTab,
    selectedRegion: String?,
    selectedDistrict: String?,
    selectedAnimalSpecies: List<AnimalSpecies>?,
    onApplyFilter: (String?, String?, List<AnimalSpecies>?) -> Unit,
    onCurrentRegionDetectButtonClicked: () -> Unit
) {
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
    }

    BackHandler {
        onDismissRequest()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
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
            onApplyFilter = {
                onApplyFilter(
                    currentSelectedRegion,
                    currentSelectedDistrict,
                    currentSelectedAnimalSpecies
                )
            },
            onCurrentRegionDetectButtonClicked = onCurrentRegionDetectButtonClicked
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
    onApplyFilter: () -> Unit,
    onCurrentRegionDetectButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        TabRow(
            selectedTabIndex = currentTab.ordinal,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(horizontal = spacingMedium),
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
                    size = iconSizeMedium,
                    tint = colorScheme.icon.medium
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = colorScheme.border.verySubtle
        )

        Box(modifier = Modifier.heightIn(min = 300.dp, max = 400.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                            onSpeciesSelected = { updatedList ->
                                onSpeciesSelected(updatedList)
                            }
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium, vertical = spacingXXL)
                    .align(Alignment.BottomCenter)
            ) {
                BasicButton(
                    text = "내 위치",
                    leadingIcon = IconResource.Vector(Icons.Outlined.LocationSearching),
                    leadingIconSize = iconSizeLarge,
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 16.dp,
                    onClicked = onCurrentRegionDetectButtonClicked,
                )
                BasicButton(
                    text = "병원 보기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 16.dp,
                    onClicked = onApplyFilter,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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
    val isSelectedAll = (selectedDistrict == regions[selectedRegion]!!.first())

    if (selectedDistrict.isNullOrEmpty()) {
        onRegionSelected(selectedRegion, regions[selectedRegion]!!.first())
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacingSmall)
    ) {
        LazyColumn {
            items(regions.keys.toList()) { region ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
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
                                onRegionSelected(region, regions[region]!!.first())
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
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { onRegionSelected(selectedRegion, district) }
                            )
                            .padding(horizontal = spacingLarge, vertical = spacingSmall),
                        fontWeight = if (selectedDistrict == district || isSelectedAll) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedDistrict == district || isSelectedAll) colorScheme.action.primary.default
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
    onSpeciesSelected: (List<AnimalSpecies>) -> Unit
) {
    val speciesByCategory = remember { animalSpecies.groupBy { it.category } }

    LazyColumn(modifier = Modifier.padding(horizontal = spacingLarge)) {
        speciesByCategory.forEach { (category, speciesList) ->
            val isSelectedAll = (selectedAnimalSpecies?.containsAll(speciesList) ?: false)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.toKorean(),
                        style = MaterialTheme.typography.bodyMedium.emp(),
                        color = colorScheme.text.secondary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "✓ 전체선택",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSelectedAll) colorScheme.action.primary.default else colorScheme.text.tertiary,
                        modifier = Modifier.clickable {
                            val currentSelected = selectedAnimalSpecies.orEmpty().toMutableList()
                            val allSpeciesInCategorySelected =
                                speciesList.all { currentSelected.contains(it) }

                            if (allSpeciesInCategorySelected) {
                                currentSelected.removeAll(speciesList)
                            } else {
                                currentSelected.addAll(speciesList)
                            }
                            onSpeciesSelected(currentSelected.distinct())
                        }
                    )
                }
            }
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    speciesList.forEach { species ->
                        val isSelected = selectedAnimalSpecies?.contains(species) ?: false
                        val color =
                            if (isSelected) colorScheme.border.active else colorScheme.border.tertiary
                        BasicChip(
                            text = species.toKorean(),
                            onClick = {
                                val currentSelected =
                                    selectedAnimalSpecies.orEmpty().toMutableList()
                                if (currentSelected.contains(species)) {
                                    currentSelected.remove(species)
                                } else {
                                    currentSelected.add(species)
                                }
                                onSpeciesSelected(currentSelected)
                            },
                            borderColor = color,
                            textColor = color
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Pixel 9", device = Devices.PIXEL_9)
@Preview(name = "Pixel 7 Pro (Wide)", device = Devices.PIXEL_7_PRO)
@Preview(name = "Pixel 5 (Normal)", device = Devices.PIXEL_5)
@Preview(name = "Pixel 3 (Small)", device = Devices.PIXEL_3)
@Preview(name = "Default", device = PHONE)
@Composable
fun HospitalFilterBottomSheetPreview() {
    val regionsMap = remember { Region.entries.associate { it.displayName to it.districts } }

    PetbulanceTheme {
        HospitalBottomSheetContents(
            currentTab = FilterTab.ANIMAL_SPECIES,
            onSelectedTabChanged = {},
            regionsMap = regionsMap,
            animalSpecies = AnimalSpecies.entries,
            selectedRegion = "서울시",
            selectedDistrict = "강남구",
            selectedAnimalSpecies = null,
            onRegionSelected = { _, _ -> },
            onSpeciesSelected = {},
            resetFilters = {},
            onApplyFilter = {},
            onCurrentRegionDetectButtonClicked = {}
        )
    }
}
