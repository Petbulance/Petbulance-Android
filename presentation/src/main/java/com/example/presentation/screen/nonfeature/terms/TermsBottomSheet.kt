package com.example.presentation.screen.nonfeature.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermDetails
import com.example.presentation.R
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.ButtonType
import com.example.presentation.utils.error.ErrorDialog
import com.example.presentation.utils.error.ErrorDialogState
import kotlinx.coroutines.CoroutineScope
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsBottomSheet(
    argument: TermsArgument,
    data: TermsData,
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var errorDialogState by remember { mutableStateOf(ErrorDialogState.idle()) }

    val termList = data.termList
    val termDetails = data.termDetails
    val termUiModelList = termList.map { it.toUiModel() }

    LaunchedEffect(argument.event) {
        argument.event.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
        ) {
            TermsBottomSheetContents(
                termList = termUiModelList,
                termDetails = termDetails,
                onTermCheckedChange = { termId, isChecked ->
                    argument.intent(TermsIntent.OnTermCheckedChange(termId, isChecked))
                },
                onTermsAgree = {
                    argument.intent(TermsIntent.OnAgreementButtonClicked)
                }
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

    // BackHandler { /* TODO : BackHandler? */ }
}

@Composable
private fun TermsBottomSheetContents(
    termList: List<TermUiModel>,
    termDetails: List<TermDetails>,
    onTermCheckedChange: (String, Boolean) -> Unit,
    onTermsAgree: () -> Unit,
) {
    val expandedState = remember { mutableStateOf(setOf("1")) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "펫뷸런스를 쓰러면 동의가 필요해요",
                color = PetbulanceTheme.colorScheme.text.secondary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        termList.forEach { term ->
            val isExpanded = expandedState.value.contains(term.id)

            item(key = term.id) {
                TermHeader(
                    term = term,
                    isExpanded = isExpanded,
                    onCheckedChange = { isChecked -> onTermCheckedChange(term.id, isChecked) },
                    onExpandToggle = {
                        expandedState.value = if (isExpanded) {
                            expandedState.value - term.id
                        } else {
                            expandedState.value + term.id
                        }
                    }
                )
            }

            if (isExpanded) {
                item(key = "${term.id}_details") {
                    TermDetailsContent(
                        details = termDetails.find { it.id == term.id }?.content
                            ?: "약관 전문을 불러오는데 실패했어요."
                    )
                }
            }
        }
        item {
            BasicButton(
                text = "동의하기",
                type = ButtonType.PRIMARY,
                onClicked = { onTermsAgree() },
                modifier = Modifier.padding(top = 20.dp)
            )
            BasicButton(
                text = "닫기",
                type = ButtonType.DEFAULT,
                onClicked = { onTermsAgree() },
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun TermHeader(
    term: TermUiModel,
    isExpanded: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onExpandToggle: () -> Unit
) {
    var isChecked by remember { mutableStateOf(term.isChecked) }
    val iconTint =
        if (isChecked) PetbulanceTheme.colorScheme.action.link.default else PetbulanceTheme.colorScheme.text.disabled
    val requirementSuffix = if (term.required) "[필수] " else "[선택] "

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.check_icon),
                contentDescription = "Checked",
                tint = iconTint,
                modifier = Modifier
                    .size(16.dp)
                    .clickable {
                        isChecked = !isChecked
                        onCheckedChange(isChecked)
                    }
            )
            Text(
                text = requirementSuffix + term.title,
                style = MaterialTheme.typography.labelLarge,
                color = PetbulanceTheme.colorScheme.text.tertiary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "Toggle term detail",
            tint = PetbulanceTheme.colorScheme.text.tertiary,
            modifier = Modifier
                .size(16.dp)
                .clickable { onExpandToggle() }
        )
    }
}

@Composable
private fun TermDetailsContent(details: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 24.dp)
            .heightIn(max = 200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = details,
            style = MaterialTheme.typography.labelSmall,
            color = PetbulanceTheme.colorScheme.text.caption
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(apiLevel = 34)
@Composable
private fun TermsBottomSheetScreenPreview() {
    val termList = listOf(
        Term(
            id = "1",
            title = "어차피 안 읽고 동의할 약관",
            required = true,
            summary = "모두가 안 읽을 것임은 널리 알려진 사실입니다.",
            version = "2025-01-01",
            lastUpdated = Instant.now()
        ),
        Term(
            id = "2",
            title = "얘는 어차피 안 읽고 동의도 안할 약관",
            required = false,
            summary = "모두가 안 읽을 것임은 널리 알려진 사실입니다.",
            version = "2025-01-02",
            lastUpdated = Instant.now()
        )
    )
    val termDetails = listOf(
        TermDetails(
            id = "1",
            title = "어차피 안 읽고 동의할 약관",
            required = true,
            version = "2025-01-01",
            content = "이거 동의 안하면 어차피 서비스 이용 못하지 않아요?" +
                    "하지만 동의를 받지 않는다면 추후 책임을 물게 될 소지가 발생하기 때문에 " +
                    "고객에게 책임을 떠넘긴다는 비밀스러운 이유를 간직하면서도 필수 약관이 존재하는 것이겠죠?"
        ),
        TermDetails(
            id = "2",
            title = "얘는 어차피 안 읽고 동의도 안할 약관",
            required = true,
            version = "2025-01-02",
            content = "안읽고, 동의도 안 할거라는 것을 안다면" +
                    "이 약관은 왜 존재하는 것일까요? 그래도 나름대로의 존재 이유가 있겠죠?" +
                    "이 약관은 관측되기 전에는 존재할 수도, 존재하지 않을 수도 있는 약관일 것입니다만" +
                    "결과론적으로 모두가 동의하지 않을 것이기에 큰 의미는 없을 것 같아요."
        )
    )

    PetbulanceTheme {
        TermsBottomSheetContents(
            termList = termList.map { it.toUiModel() },
            termDetails = termDetails,
            onTermCheckedChange = { _, _ -> },
            onTermsAgree = {}
        )
    }
}