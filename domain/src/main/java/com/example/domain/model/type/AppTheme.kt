package com.example.domain.model.type

enum class AppTheme {
    DEVICE,
    DAYLIGHT,
    DARK,
}

fun String.changeAppThemeToKorean(): String =
    this.replace("DEVICE", "기기 설정")
        .replace("DAYLIGHT", "일반")
        .replace("DARK", "다크 모드")

fun String.toAppTheme(): AppTheme {
    return AppTheme.valueOf(this.uppercase())
}