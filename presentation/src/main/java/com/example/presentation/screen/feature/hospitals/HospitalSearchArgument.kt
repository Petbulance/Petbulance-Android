package com.example.presentation.screen.feature.hospitals

import android.content.Context
import com.example.domain.model.common.MapBounds
import com.example.domain.usecase.feature.hospitals.SearchHospitalParams
import com.example.presentation.utils.error.ErrorEvent
import kotlinx.coroutines.flow.SharedFlow

data class HospitalSearchArgument(
    val intent: (HospitalSearchIntent) -> Unit,
    val state: HospitalSearchState,
    val event: SharedFlow<HospitalSearchEvent>
)

sealed class HospitalSearchState {
    data object Init : HospitalSearchState()
    data object OnProgress : HospitalSearchState()
}

sealed class HospitalSearchIntent {

    data class FetchCurrentLocation(val context: Context): HospitalSearchIntent()

    data class SearchNearByHospitals(val bounds: MapBounds): HospitalSearchIntent()

    data class UpdateQuery(val newQuery: SearchHospitalParams): HospitalSearchIntent()

    data class UpdateSelectedHospital(val selectedHospitalId: Long) : HospitalSearchIntent()

    data object MoveCameraToCurrentLocation : HospitalSearchIntent()

    data object LoadMore : HospitalSearchIntent()
}

sealed class HospitalSearchEvent {
    sealed class DataFetch : HospitalSearchEvent() {
        data class Error(
            override val userMessage: String = "문제가 발생했습니다.",
            override val exceptionMessage: String?
        ) : DataFetch(), ErrorEvent
    }

    sealed class Permission : HospitalSearchEvent() {
        data object LackOfPermission: Permission()
        data object NoPermissionGranted: Permission()
    }
}