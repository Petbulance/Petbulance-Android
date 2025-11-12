package com.example.presentation.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun checkUserLoginStatus(
    onLoggedIn: () -> Unit,
    onNotLoggedIn: () -> Unit
) {
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    if (user == null) {
        onNotLoggedIn()
    } else {
        onLoggedIn()
    }
}