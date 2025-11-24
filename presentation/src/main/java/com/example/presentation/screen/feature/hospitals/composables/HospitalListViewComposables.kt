package com.example.presentation.screen.feature.hospitals.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.Hospital
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.spacingMedium
import com.example.presentation.screen.feature.hospitals.HospitalCard

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