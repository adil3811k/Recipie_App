package com.example.recipieapp.viewmodel

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignViewModel: ViewModel() {
    fun signIn(credential: Credential){
        try {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleCredentinal = GoogleIdTokenCredential.createFrom(credential.data)
                val firebasecredential = GoogleAuthProvider.getCredential(googleCredentinal.idToken,null)
                Firebase.auth.signInWithCredential(firebasecredential)
            }
        }catch (e: Exception){
            Log.d("Something", "went wrong ${e.message}")
        }
    }
}