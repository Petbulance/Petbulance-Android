package com.example.presentation.screen.feature.hospitals

import android.Manifest
import android.Manifest.permission_group.PHONE
import android.location.Location
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.common.MapBounds
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.type.HospitalSortOption
import com.example.domain.model.type.toKorean
import com.example.domain.usecase.feature.hospitals.SearchHospitalParams
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.ui.RoundedCorner
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicFab
import com.example.presentation.component.ui.atom.BasicFabType
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMedium
import com.example.presentation.component.ui.molecule.SortOptionDialog
import com.example.presentation.component.ui.organism.AppTopBar
import com.example.presentation.component.ui.organism.BottomNavigationBar
import com.example.presentation.component.ui.organism.CurrentBottomNav
import com.example.presentation.component.ui.organism.TopBarAlignment
import com.example.presentation.component.ui.organism.TopBarInfo
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.utils.NaverMapView
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.naver.maps.map.NaverMap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.math.abs

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HospitalSearchScreen(
    navController: NavController,
    argument: HospitalSearchArgument,
    data: HospitalSearchData
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }
    val context = LocalContext.current

    var currentSortOption by remember { mutableStateOf(HospitalSortOption.DISTANCE) }

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    var isFineLocationPermissionRequestDialogVisible by remember { mutableStateOf(false) }

    val currentLocation = data.currentLocation
    val hospitalsResult = data.hospitalsResult
    val selectedHospitalId = data.currentSelectedHospitalId
    val searchHospitalParams = data.searchHospitalParams
    val cameraPosition = data.cameraPosition
    val isLastPage = data.isLastPage

    var isOpenedHospitalOnly by remember { mutableStateOf(searchHospitalParams.openNowOnly) }
    var isSortingDialogVisible by remember { mutableStateOf(false) }
    var isFilterBottomSheetVisible by remember { mutableStateOf(false) }
    var initialFilterTab by remember { mutableStateOf(FilterTab.REGION) }

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            argument.intent(HospitalSearchIntent.FetchCurrentLocation(context))
            isFineLocationPermissionRequestDialogVisible = false
        }
    }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                is HospitalSearchEvent.Permission.LackOfPermission -> {
                    isFineLocationPermissionRequestDialogVisible = true
                }

                is HospitalSearchEvent.Permission.NoPermissionGranted -> {
                    Log.d("siria22", "No permissionGranted")
                    isFineLocationPermissionRequestDialogVisible = true
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                topBarInfo = TopBarInfo(
                    text = "병원 검색",
                    textAlignment = TopBarAlignment.START,
                    isLeadingIconAvailable = false,
                    trailingIcons = listOf(
                        Pair(
                            IconResource.Drawable(R.drawable.search),
                            { /* TODO : search logic */ }
                        )
                    )
                ),
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = CurrentBottomNav.HOSPITAL,
                navController = navController
            )
        },
        containerColor = colorScheme.bg.frame.subtle
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HospitalSearchScreenContents(
                currentLocation = currentLocation,
                hospitalsResult = hospitalsResult,
                isOpenedHospitalOnly = isOpenedHospitalOnly,
                cameraPosition = cameraPosition,
                selectedHospitalId = selectedHospitalId,
                isLastPage = isLastPage,
                currentSortOption = currentSortOption,
                onMapBoundsChange = { bounds ->
                    argument.intent(HospitalSearchIntent.SearchNearByHospitals(bounds = bounds))
                },
                onMarkerClicked = { hospitalId ->
                    argument.intent(HospitalSearchIntent.UpdateSelectedHospital(hospitalId))
                },
                onLoadMore = {
                    argument.intent(HospitalSearchIntent.LoadMore)
                },
                onRegionChipClicked = {
                    initialFilterTab = FilterTab.REGION
                    isFilterBottomSheetVisible = true
                },
                onSpeciesChipClicked = {
                    initialFilterTab = FilterTab.ANIMAL_SPECIES
                    isFilterBottomSheetVisible = true
                },
                onSortingOptionChipClicked = {
                    isSortingDialogVisible = true
                },
                onIsOpenFilterChipClicked = { isOpenedHospitalOnly = !isOpenedHospitalOnly },
                onMoveToCurrentLocationFabClicked = { argument.intent(HospitalSearchIntent.MoveCameraToCurrentLocation) },

                )
        }
    }

    if (isFineLocationPermissionRequestDialogVisible) {
        FineLocationPermissionRequestDialog(
            onShowTermLinkClicked = { /* TODO : 뭔지 모르지만 일단 어딘가로 이동시킴 */ },
            onAgreeButtonClicked = { locationPermissionState.launchPermissionRequest() },
            onDismissButtonClicked = { isFineLocationPermissionRequestDialogVisible = false },
        )
    }

    if (isSortingDialogVisible) {
        SortOptionDialog(
            currentSortOption = currentSortOption,
            onDismissRequest = { isSortingDialogVisible = false },
            onSortOptionChanged = { newOption ->
                currentSortOption = newOption
            }
        )
    }

    if (isFilterBottomSheetVisible) {
        HospitalFilterBottomSheet(
            onDismissRequest = { isFilterBottomSheetVisible = false },
            initialTab = initialFilterTab,
            selectedRegion = searchHospitalParams.region,
            selectedDistrict = searchHospitalParams.district,
            selectedAnimalSpecies = searchHospitalParams.animal,
            onApplyFilter = { region, district, animalSpecies ->
                argument.intent(
                    HospitalSearchIntent.UpdateQuery(
                        newQuery = SearchHospitalParams(
                            q = null,
                            region = region,
                            district = district,
                            animal = animalSpecies
                        )
                    )
                )
                isFilterBottomSheetVisible = false
            },
            onResetFilter = { },
            onCurrentRegionDetectButtonClicked = {},
        )
    }

    if (errorDialogState.isErrorDialogVisible) {
        ErrorDialog(
            errorDialogState = errorDialogState,
            errorHandler = {
                errorDialogState = errorDialogState.toggleVisibility()
            }
        )
    }
}

@Composable
private fun HospitalSearchScreenContents(
    currentLocation: Location,
    selectedHospitalId: Long,
    hospitalsResult: List<Hospital>,
    isOpenedHospitalOnly: Boolean,
    cameraPosition: Location,
    isLastPage: Boolean,
    currentSortOption: HospitalSortOption,
    onMapBoundsChange: (MapBounds) -> Unit,
    onMarkerClicked: (Long) -> Unit,
    onLoadMore: () -> Unit,
    onRegionChipClicked: () -> Unit,
    onSpeciesChipClicked: () -> Unit,
    onSortingOptionChipClicked: () -> Unit,
    onIsOpenFilterChipClicked: () -> Unit,
    onMoveToCurrentLocationFabClicked: () -> Unit
) {
    var isMapListOpened by remember { mutableStateOf(false) }

    val hospitals = when (currentSortOption) {
        HospitalSortOption.DISTANCE -> hospitalsResult.sortedBy { it.distanceMeters }
        HospitalSortOption.RATINGS -> hospitalsResult.sortedByDescending { it.rating }
        HospitalSortOption.REVIEWS -> hospitalsResult.sortedByDescending { it.reviewCount }
    }.filter {
        if (isOpenedHospitalOnly) it.isOpenNow else true
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (hospitalsResult.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.image_no_result),
                    contentDescription = "no result image",
                    modifier = Modifier.fillMaxSize(0.22f)
                )
                Text(
                    text = "주변 병원을 찾을 수 없어요.",
                    style = MaterialTheme.typography.titleSmall,
                    color = colorScheme.text.tertiary,
                    modifier = Modifier.padding(bottom = spacingSmall)
                )
                Text(
                    text = "좀 더 넓은 지역으로 검색해보세요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.text.tertiary,
                )
            }
        } else {

            var naverMap by remember { mutableStateOf<NaverMap?>(null) }

            if (isMapListOpened) {
                MapListViewer(
                    hospitalList = hospitals,
                    rowFilters = {
                        RowChipFilters(
                            currentSortOption = currentSortOption,
                            isOpenedHospitalOnly = isOpenedHospitalOnly,
                            onRegionChipClicked = onRegionChipClicked,
                            onSpeciesChipClicked = onSpeciesChipClicked,
                            onSortingOptionChipClicked = onSortingOptionChipClicked,
                            onIsOpenFilterChipClicked = onIsOpenFilterChipClicked
                        )
                    },
                    isLastPage = isLastPage,
                    onLoadMore = onLoadMore
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = spacingMedium, vertical = spacingXL)
                ) {
                    FabIcons(
                        isMapListOpened = isMapListOpened,
                        onOpenListButtonClicked = { isMapListOpened = !isMapListOpened },
                        onMoveToCurrentLocationFabClicked = onMoveToCurrentLocationFabClicked
                    )
                }
            } else {
                NaverMapView(
                    currentLocation = currentLocation,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    places = hospitals.map { it.toMarker() },
                    onMapReady = { map -> naverMap = map },
                    onMapBoundsChange = {},
                    selectedHospitalId = selectedHospitalId,
                    onMarkerClicked = onMarkerClicked,
                    cameraPosition = cameraPosition
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!isMapListOpened) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RowChipFilters(
                                currentSortOption = currentSortOption,
                                isOpenedHospitalOnly = isOpenedHospitalOnly,
                                onRegionChipClicked = onRegionChipClicked,
                                onSpeciesChipClicked = onSpeciesChipClicked,
                                onSortingOptionChipClicked = onSortingOptionChipClicked,
                                onIsOpenFilterChipClicked = onIsOpenFilterChipClicked
                            )
                            BasicButton(
                                text = "현 지도에서 검색",
                                size = BasicButtonSize.XS,
                                leadingIcon = IconResource.Vector(Icons.Default.Refresh),
                                buttonType = BasicButtonType.SECONDARY,
                                radius = 1000.dp,
                                onClicked = {
                                    val bounds = naverMap?.contentBounds
                                    if (bounds != null) {
                                        val domainBounds = MapBounds(
                                            minLat = bounds.southWest.latitude,
                                            minLng = bounds.southWest.longitude,
                                            maxLat = bounds.northEast.latitude,
                                            maxLng = bounds.northEast.longitude
                                        )
                                        onMapBoundsChange(domainBounds)
                                    }
                                }
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FabIcons(
                            isMapListOpened = isMapListOpened,
                            onOpenListButtonClicked = { isMapListOpened = !isMapListOpened },
                            onMoveToCurrentLocationFabClicked = onMoveToCurrentLocationFabClicked
                        )

                        if (hospitals.isNotEmpty() && !isMapListOpened) {
                            HospitalCarousel(
                                hospitals = hospitals,
                                selectedHospitalId = selectedHospitalId,
                                onHospitalSelected = { hospitalId ->
                                    onMarkerClicked(hospitalId)
                                },
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowChipFilters(
    currentSortOption: HospitalSortOption,
    isOpenedHospitalOnly: Boolean,
    onRegionChipClicked: () -> Unit,
    onSpeciesChipClicked: () -> Unit,
    onSortingOptionChipClicked: () -> Unit,
    onIsOpenFilterChipClicked: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingXS),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(spacingMedium)
    ) {
        BasicChip(
            text = "지역",
            showLeadingIcon = false,
            showTrailingIcon = true,
            onClick = onRegionChipClicked
        )

        BasicChip(
            text = "동물종",
            showLeadingIcon = false,
            showTrailingIcon = true,
            onClick = onSpeciesChipClicked
        )

        BasicChip(
            text = currentSortOption.toKorean(),
            showLeadingIcon = true,
            showTrailingIcon = false,
            onClick = onSortingOptionChipClicked
        )
        val color = if (isOpenedHospitalOnly) colorScheme.tag.blue.medium
        else colorScheme.text.tertiary

        BasicChip(
            text = "진료중",
            showLeadingIcon = false,
            showTrailingIcon = false,
            borderColor = color,
            textColor = color,
            dropShadow = true,
            onClick = onIsOpenFilterChipClicked
        )
    }
}

@Composable
private fun FabIcons(
    modifier: Modifier = Modifier,
    isMapListOpened: Boolean,
    onOpenListButtonClicked: () -> Unit,
    onMoveToCurrentLocationFabClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacingMedium)
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            if (isMapListOpened) {
                BasicFab(
                    text = "지도보기",
                    leadingIcon = IconResource.Drawable(R.drawable.icon_map),
                    type = BasicFabType.PRIMARY,
                    onClick = onOpenListButtonClicked
                )
            } else {
                BasicFab(
                    text = "목록보기",
                    leadingIcon = IconResource.Vector(Icons.AutoMirrored.Filled.List),
                    type = BasicFabType.SECONDARY,
                    onClick = onOpenListButtonClicked
                )
            }
        }

        if (!isMapListOpened) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                BasicIcon(
                    iconResource = IconResource.Vector(Icons.Default.LocationSearching),
                    contentDescription = "Center user location",
                    size = iconSizeMedium,
                    tint = colorScheme.icon.basic,
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCorner,
                            clip = false
                        )
                        .background(
                            color = colorScheme.bg.frame.default,
                            shape = RoundedCorner
                        )
                        .padding(spacingXS)
                        .clickable { onMoveToCurrentLocationFabClicked() }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HospitalCarousel(
    hospitals: List<Hospital>,
    selectedHospitalId: Long,
    onHospitalSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(selectedHospitalId, hospitals) {
        val selectedIndex = hospitals.indexOfFirst { it.hospitalId == selectedHospitalId }
        if (selectedIndex != -1) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
            val centralItem = visibleItemsInfo.minByOrNull {
                abs(it.offset + it.size / 2)
            }
            if (centralItem != null) {
                val centralHospitalId = hospitals[centralItem.index].hospitalId
                if (centralHospitalId != selectedHospitalId) {
                    onHospitalSelected(centralHospitalId)
                }
            }
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacingMedium),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        flingBehavior = flingBehavior
    ) {
        items(hospitals, key = { it.hospitalId }) { hospital ->
            HospitalCard(hospital = hospital, modifier = Modifier.fillParentMaxWidth())
        }
    }
}


@Preview(apiLevel = 34)
@Preview(name = "Pixel 9", device = Devices.PIXEL_9)
@Preview(name = "Pixel 7 Pro (Wide)", device = Devices.PIXEL_7_PRO)
@Preview(name = "Pixel 5 (Normal)", device = Devices.PIXEL_5)
@Preview(name = "Pixel 3 (Small)", device = Devices.PIXEL_3)
@Preview(name = "Default", device = PHONE)
@Composable
private fun HospitalSearchScreenPreview() {
    PetbulanceTheme {
        HospitalSearchScreen(
            navController = rememberNavController(),
            argument = HospitalSearchArgument(
                intent = { },
                state = HospitalSearchState.Init,
                event = MutableSharedFlow()
            ),
            data = HospitalSearchData.stub()//.copy(hospitalsResult = emptyList())
        )
    }
}