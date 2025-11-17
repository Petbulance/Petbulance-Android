package com.example.presentation.screen.feature.hospitals

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.domain.model.common.MapBounds
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.usecase.feature.hospitals.SearchHospitalParams
import com.example.domain.usecase.feature.hospitals.SearchHospitalsUseCase
import com.example.presentation.utils.BaseViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class HospitalSearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val searchHospitalsUseCase: SearchHospitalsUseCase
) : BaseViewModel() {

    private val defaultLocation = Location("default").apply {
        latitude = 37.5665
        longitude = 126.9780
    }

    private val _state = MutableStateFlow<HospitalSearchState>(HospitalSearchState.Init)
    val state: StateFlow<HospitalSearchState> = _state

    private val _eventFlow = MutableSharedFlow<HospitalSearchEvent>(replay = 1)
    val eventFlow: SharedFlow<HospitalSearchEvent> = _eventFlow

    private val _hospitalsResult = MutableStateFlow<List<Hospital>>(emptyList())
    val hospitalsResult: StateFlow<List<Hospital>> = _hospitalsResult

    private val _currentLocation = MutableStateFlow(defaultLocation)
    val currentLocation: StateFlow<Location> = _currentLocation

    private val _searchHospitalParams = MutableStateFlow(SearchHospitalParams())
    val searchHospitalParams: StateFlow<SearchHospitalParams> = _searchHospitalParams

    private val _currentSelectedHospitalId = MutableStateFlow(0L)
    val currentSelectedHospitalId: StateFlow<Long> = _currentSelectedHospitalId

    private val _cameraPosition = MutableStateFlow(defaultLocation)
    val cameraPosition: StateFlow<Location> = _cameraPosition

    private val _isLastPage = MutableStateFlow(false)
    val isLastPage: StateFlow<Boolean> = _isLastPage

    private var currentPage = 0
    private var lastQuery: SearchHospitalParams? = null

    private var searchJob: Job? = null


    fun onIntent(intent: HospitalSearchIntent) {
        when (intent) {
            is HospitalSearchIntent.FetchCurrentLocation -> {
                launch { checkAndFetchLocation(intent.context) }
            }

            is HospitalSearchIntent.UpdateQuery -> {
                launch { onQueryChanged(intent.newQuery) }
            }

            is HospitalSearchIntent.SearchNearByHospitals -> {
                launch { searchNearByHospitals(intent.bounds) }
            }

            is HospitalSearchIntent.UpdateSelectedHospital -> {
                _currentSelectedHospitalId.value = intent.selectedHospitalId
            }

            is HospitalSearchIntent.MoveCameraToCurrentLocation -> {
                val newLocation = Location("newLocation").apply {
                    latitude = _currentLocation.value.latitude
                    longitude = _currentLocation.value.longitude
                    time = System.currentTimeMillis()
                }
                _cameraPosition.value = newLocation
            }

            is HospitalSearchIntent.LoadMore -> {
                searchHospital(isNewQuery = false)
            }
        }
    }

    init {
        launch {
            checkAndFetchLocation(context)
        }
    }

    private suspend fun checkAndFetchLocation(context: Context) {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        when {
            hasFineLocationPermission -> {
                val fetchedLocation = fetchCurrentCoordinate(context)
                _currentLocation.value = fetchedLocation
                _cameraPosition.value = fetchedLocation

                _searchHospitalParams.value = _searchHospitalParams.value.copy(
                    lat = fetchedLocation.latitude,
                    lng = fetchedLocation.longitude
                )

                searchHospital(isNewQuery = true)
            }

            hasCoarseLocationPermission -> {
                launch {
                    _eventFlow.emit(HospitalSearchEvent.Permission.LackOfPermission)
                }
            }

            else -> {
                launch {
                    _eventFlow.emit(HospitalSearchEvent.Permission.NoPermissionGranted)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun fetchCurrentCoordinate(context: Context): Location {
        return suspendCancellableCoroutine { cont ->
            val client = LocationServices.getFusedLocationProviderClient(context)
            client.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(location)
                    } else {
                        cont.resume(defaultLocation)
                    }
                }
                .addOnFailureListener {
                    cont.resume(defaultLocation)
                }
        }
    }

    private suspend fun searchNearByHospitals(boundary: MapBounds) {
        _state.value = HospitalSearchState.OnProgress
        runCatching {
            _searchHospitalParams.value = SearchHospitalParams(
                lat = _currentLocation.value.latitude,
                lng = _currentLocation.value.longitude,
                bounds = boundary
            )
            searchHospital(isNewQuery = true)
        }.onFailure { ex ->
            _eventFlow.emit(
                HospitalSearchEvent.DataFetch.Error(
                    userMessage = "병원 정보를 불러오는데 실패했어요.",
                    exceptionMessage = ex.message
                )
            )
        }
        _state.value = HospitalSearchState.Init
    }

    private fun onQueryChanged(newParams: SearchHospitalParams) {
        if (newParams == _searchHospitalParams.value) return
        _searchHospitalParams.value = newParams
        searchHospital(isNewQuery = true)
    }

    private fun searchHospital(isNewQuery: Boolean = false) {

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (isNewQuery) {
                currentPage = 0
                _isLastPage.value = false
                _hospitalsResult.value = emptyList()
                lastQuery = _searchHospitalParams.value.copy()
            } else {
                if (_isLastPage.value || _state.value == HospitalSearchState.OnProgress) return@launch
                currentPage += 1
            }

            val params = _searchHospitalParams.value.copy(page = currentPage)
            _state.value = HospitalSearchState.OnProgress

            runCatching {
                searchHospitalsUseCase(params)
            }.onSuccess { result ->
                val pageData = result.getOrThrow()
                _hospitalsResult.value = if (isNewQuery) {
                    pageData.content
                } else {
                    _hospitalsResult.value + pageData.content
                }
                _isLastPage.value = pageData.isLast
                _state.value = HospitalSearchState.Init
            }.onFailure { ex ->
                _eventFlow.emit(
                    HospitalSearchEvent.DataFetch.Error(
                        userMessage = "병원 정보들을 불러오는데 실패했습니다.",
                        exceptionMessage = ex.message
                    )
                )
                _state.value = HospitalSearchState.Init
            }
        }
    }

}
