package com.example.presentation.screen.feature.hospitals

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.model.type.Region
import com.example.domain.model.type.toKorean
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalFilterBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    intent: (HospitalFilterIntent) -> Unit,
    initialTab: FilterTab,
    selectedRegion: String?,
    selectedDistrict: String?,
    selectedAnimalSpecies: List<AnimalSpecies>?
) {
    val coroutineScope = rememberCoroutineScope()

    val regionsMap = remember { Region.entries.associate { it.displayName to it.districts } }
    val animalSpecies = remember { AnimalSpecies.entries }

    var currentTab by remember { mutableStateOf(initialTab) }
    var currentSelectedRegion by remember { mutableStateOf(selectedRegion ?: regionsMap.keys.first()) }
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
        containerColor = Color.White,
        modifier = Modifier.heightIn(min = 24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TabRow(selectedTabIndex = currentTab.ordinal) {
                Tab(
                    selected = currentTab == FilterTab.REGION,
                    onClick = { currentTab = FilterTab.REGION },
                    text = { Text("지역") }
                )
                Tab(
                    selected = currentTab == FilterTab.ANIMAL_SPECIES,
                    onClick = { currentTab = FilterTab.ANIMAL_SPECIES },
                    text = { Text("동물종") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Row(
                    modifier = Modifier.clickable { resetFilters() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("선택 초기화")
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.Refresh),
                        contentDescription = "reset"
                    )
                }
            }

            Box(modifier = Modifier.heightIn(min = 300.dp, max = 400.dp)) {
                when (currentTab) {
                    FilterTab.REGION -> {
                        RegionSelection(
                            regions = regionsMap,
                            selectedRegion = currentSelectedRegion,
                            selectedDistrict = currentSelectedDistrict,
                            onRegionSelected = { region, district ->
                                currentSelectedRegion = region
                                currentSelectedDistrict = district
                            }
                        )
                    }

                    FilterTab.ANIMAL_SPECIES -> {
                        AnimalSpeciesSelection(
                            animalSpecies = animalSpecies,
                            selectedAnimalSpecies = currentSelectedAnimalSpecies,
                            onSpeciesSelected = { species ->
                                val newList = currentSelectedAnimalSpecies?.toMutableList() ?: mutableListOf()
                                if (newList.contains(species)) newList.remove(species)
                                else newList.add(species)
                                currentSelectedAnimalSpecies = newList
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BasicButton(
                text = "병원 보기",
                buttonType = BasicButtonType.PRIMARY,
                size = BasicButtonSize.L,
                onClicked = {
                    intent(HospitalFilterIntent.OnApplyFilter)
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                }
            )
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
    Row {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(regions.keys.toList()) { region ->
                Text(
                    text = region,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRegionSelected(region, null) }
                        .background(if (selectedRegion == region) colorScheme.bg.frame.default else Color.Transparent)
                        .padding(16.dp),
                    fontWeight = if (selectedRegion == region) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(regions[selectedRegion] ?: emptyList()) { district ->
                Text(
                    text = district,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRegionSelected(selectedRegion, district) }
                        .background(if (selectedDistrict == district) colorScheme.bg.frame.subtle else Color.Transparent)
                        .padding(16.dp),
                    fontWeight = if (selectedDistrict == district) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedDistrict == district) colorScheme.text.primary else colorScheme.text.secondary
                )
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
