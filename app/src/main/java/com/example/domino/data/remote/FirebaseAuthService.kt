package com.example.domino.data.remote

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthService {

    private val auth = FirebaseAuth.getInstance()

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String {
        val user = auth.currentUser
        if (user == null) {
            return ""
        }
        return user.uid
    }

    fun getCurrentUserName(): String {
        val user = auth.currentUser
        if (user == null) {
            return ""
        }
        val name = user.displayName
        if (name == null) {
            return ""
        }
        return name
    }

    fun logout() {
        auth.signOut()
    }
}