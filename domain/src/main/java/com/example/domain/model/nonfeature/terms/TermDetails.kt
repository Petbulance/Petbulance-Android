package com.example.domain.model.nonfeature.terms

data class TermDetails(
    val id: String,
    val title: String,
    val required: Boolean,
    val version: String,
    val content: String
){
    companion object {
        fun stub() = TermDetails(
            id = "1",
            title = "어차피 안 읽고 동의할 약관",
            required = true,
            version = "2025-01-01",
            content = "이거 동의 안하면 어차피 서비스 이용 못하지 않아요?" +
                    "하지만 동의를 받지 않는다면 추후 책임을 물게 될 소지가 발생하기 때문에 " +
                    "고객에게 책임을 떠넘긴다는 비밀스러운 이유를 간직하면서도 필수 약관이 존재하는 것이겠죠?"
        )
    }
}
