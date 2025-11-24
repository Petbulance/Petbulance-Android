package com.example.presentation.screen.feature.hospitals

import android.content.Context
import com.example.domain.model.common.MapBounds
import com.example.domain.model.feature.hospitals.HospitalSearchParams
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HospitalSearchArgument(
    val intent: (HospitalSearchIntent) -> Unit,
    val state: HospitalSearchState,
    val event: SharedFlow<HospitalSearchEvent>
)

sealed class HospitalSearchState {
    sealed class DataState : HospitalSearchState() {
        data object Init : DataState()
        data object OnProgress : DataState()
    }

    sealed class ScreenState : HospitalSearchState() {
        data object MapView : ScreenState()
        data object ListView : ScreenState()
        data object NoResultView : ScreenState()
        data object SearchView : ScreenState()
    }
}

sealed class HospitalSearchIntent {

    data class FetchCurrentLocation(val context: Context) : HospitalSearchIntent()

    data class SearchNearByHospitals(val bounds: MapBounds) : HospitalSearchIntent()

    data class UpdateSelectedHospital(val selectedHospitalId: Long) : HospitalSearchIntent()

    data object MoveCameraToCurrentLocation : HospitalSearchIntent()

    data object LoadMore : HospitalSearchIntent()

    data class ChangeScreenState(val state: HospitalSearchState) : HospitalSearchIntent()

    data class UpdateTempFilters(val params: HospitalSearchParams) : HospitalSearchIntent()

    data object ApplyFilters : HospitalSearchIntent()

    data object OpenFilter : HospitalSearchIntent()
}

enum class FilterTab {
    REGION, ANIMAL_SPECIES
}

sealed class HospitalFilterBottomSheetState {
    data object Init : HospitalFilterBottomSheetState()
    data object OnProgress : HospitalFilterBottomSheetState()
}

sealed class HospitalSearchEvent {
    sealed class DataFetch : HospitalSearchEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    sealed class Permission : HospitalSearchEvent() {
        data object LackOfPermission : Permission()
        data object NoPermissionGranted : Permission()
    }
}