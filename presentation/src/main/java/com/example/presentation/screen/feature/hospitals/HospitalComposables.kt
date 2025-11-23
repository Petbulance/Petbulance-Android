package com.example.presentation.screen.feature.hospitals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.Hospital
import com.example.domain.model.type.AnimalSpecies
import com.example.domain.model.type.toKorean
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.LargeRoundedCorner
import com.example.presentation.component.ui.RoundedCorner
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicButtonSize
import com.example.presentation.component.ui.atom.BasicButtonType
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import com.example.presentation.component.ui.iconSizeMs
import com.example.presentation.component.ui.iconSizeSmall
import com.example.presentation.component.ui.spacingLarge
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.component.ui.spacingSmall
import com.example.presentation.component.ui.spacingXL
import com.example.presentation.component.ui.spacingXS
import com.example.presentation.component.ui.spacingXXS
import com.example.presentation.component.ui.spacingXXXS
import java.util.Locale

@Composable
fun FineLocationPermissionRequestDialog(
    onShowTermLinkClicked: () -> Unit,
    onAgreeButtonClicked: () -> Unit,
    onDismissButtonClicked: () -> Unit
) {
    BasicDialog {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "병원 검색을 이용하려면\n위치정보 이용 약관 동의가 필요해요",
                    style = MaterialTheme.typography.titleSmall,
                    color = colorScheme.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "(선택)위치 정보 이용 약관",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.text.caption
                    )
                    Text(
                        text = "약관보기",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.action.link.default,
                        modifier = Modifier.clickable { onShowTermLinkClicked() }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicButton(
                    text = "다음에",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.SECONDARY,
                    radius = 28.dp,
                    onClicked = { onDismissButtonClicked() },
                    modifier = Modifier.weight(1f)
                )
                BasicButton(
                    text = "동의하기",
                    size = BasicButtonSize.L,
                    buttonType = BasicButtonType.PRIMARY,
                    radius = 28.dp,
                    onClicked = { onAgreeButtonClicked() },
                    modifier = Modifier.weight(1f)
                )
            }
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

@Composable
fun HospitalListViewer(
    hospitalList: List<Hospital>,
    isLastPage: Boolean,
    onLoadMore: () -> Unit,
    rowFilters: @Composable () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.bg.frame.subtle)
    ) {
        item {
            rowFilters()
        }
        items(hospitalList) { hospital ->
            HospitalCard(
                hospital, Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium)
            )
        }
        if (hospitalList.isNotEmpty()) {
            item {
                Text(
                    text = if (isLastPage) "모두 표시됨" else "더보기",
                    style = MaterialTheme.typography.titleMedium.emp(),
                    color = colorScheme.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { if (!isLastPage) onLoadMore() }
                )
            }
        }
    }
}

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

@Preview(apiLevel = 34)
@Composable
private fun FineLocationPermissionRequestDialogPreview() {
    PetbulanceTheme {
        FineLocationPermissionRequestDialog({}, {}, {})
    }
}

@Preview(apiLevel = 34)
@Composable
private fun MapListViewerPreview() {
    PetbulanceTheme {
        HospitalListViewer(
            rowFilters = {},
            hospitalList = listOf(
                Hospital.stub(),
                Hospital.stub().copy(hospitalId = 2L)
            ),
            isLastPage = false,
            onLoadMore = {},
        )
    }
}