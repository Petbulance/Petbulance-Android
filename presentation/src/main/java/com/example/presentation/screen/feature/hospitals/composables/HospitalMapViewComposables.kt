package com.example.presentation.screen.feature.hospitals.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.type.toKorean
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.LargeRoundedCorner
import com.example.presentation.component.ui.RoundedCorner
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeSmall
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.component.ui.spacingXXXS
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HospitalCarousel(
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

@Composable
fun HospitalCard(
    hospital: Hospital,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = LargeRoundedCorner,
                clip = false
            )
            .background(
                color = colorScheme.bg.frame.default,
                shape = LargeRoundedCorner
            )
            .padding(
                horizontal = spacingLarge,
                vertical = spacingXL
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingXXXS),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXS),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = hospital.name,
                        style = MaterialTheme.typography.titleMedium.emp(),
                        color = colorScheme.text.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacingXXXS),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicIcon(
                            iconResource = IconResource.Drawable(R.drawable.icon_star_filled),
                            contentDescription = "Star",
                            size = iconSizeSmall,
                            tint = colorScheme.icon.rating
                        )
                        Text(
                            text = "${hospital.rating}",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.text.secondary
                        )
                        Text(
                            text = "(${hospital.reviewCount})",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.text.caption
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hospital.isOpenNow) {
                        Text(
                            text = "진료 중",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.tag.blue.medium
                        )
                    } else {
                        Text(
                            text = "진료 마감",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.text.disabled
                        )
                    }

                    Text(
                        text = "·",
                        style = MaterialTheme.typography.titleLarge.emp(),
                        color = colorScheme.text.disabled
                    )

                    Text(
                        text = hospital.openHours,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.text.primary
                    )

                    Text(
                        text = "·",
                        style = MaterialTheme.typography.titleLarge.emp(),
                        color = colorScheme.text.disabled
                    )


                    Text(
                        text = String.format(
                            Locale.US,
                            "%.1f",
                            hospital.distanceMeters / 1000.0
                        ) + " km",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.text.caption
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacingXXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color =
                        if (hospital.isOpenNow) colorScheme.tag.blue.medium else colorScheme.text.disabled
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.icon_phone_filled),
                        contentDescription = "phone",
                        size = iconSizeSmall,
                        tint = color
                    )
                    Text(
                        text = hospital.phone,
                        style = MaterialTheme.typography.labelLarge.emp(),
                        color = color
                    )
                    BasicIcon(
                        iconResource = IconResource.Drawable(R.drawable.icon_copy),
                        contentDescription = "copy",
                        size = iconSizeSmall,
                        tint = color
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                hospital.types.forEach { elem ->
                    Text(
                        text = elem.toKorean(),
                        style = MaterialTheme.typography.labelMedium.emp(),
                        color = colorScheme.text.secondary,
                        modifier = Modifier
                            .background(
                                color = colorScheme.tag.yellow.subtle,
                                shape = RoundedCorner
                            )
                            .padding(horizontal = spacingXS, vertical = spacingXXS)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = spacingXXS))
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalCardPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HospitalCard(Hospital.stub())
            HospitalCard(Hospital.stub().copy(isOpenNow = false))
        }
    }
}
