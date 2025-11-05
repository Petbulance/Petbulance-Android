package com.example.presentation.screen.nonfeature.terms

import com.example.domain.model.nonfeature.terms.Term
import com.example.domain.model.nonfeature.terms.TermDetails
import java.time.Instant
import kotlin.time.ExperimentalTime

data class TermsData(
    val termList: List<Term>,
    val termDetails: List<TermDetails>,
    val isAllRequiredTermsAgreed: Boolean
) {
    companion object {
        fun empty() = TermsData(termList = emptyList(), termDetails = emptyList(),
            false)
    }
}

data class TermUiModel @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val title: String,
    val required: Boolean,
    val summary: String,
    val version: String,
    val lastUpdated: Instant,
    val isChecked: Boolean
)

@OptIn(ExperimentalTime::class)
fun TermUiModel.toDomain() = Term(
    id = id,
    title = title,
    required = required,
    summary = summary,
    version = version,
    lastUpdated = lastUpdated
)

fun Term.toUiModel() = TermUiModel(
    id = id,
    title = title,
    required = required,
    summary = summary,
    version = version,
    lastUpdated = lastUpdated,
    isChecked = false
)