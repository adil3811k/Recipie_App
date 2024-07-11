package com.example.recipieapp.sigin

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class GoogleAuthCalint (
    private val context: Context,
    private val oneTabClint: SignInClient
){
    private val auth = Firebase.auth
    suspend fun sigIn(): IntentSender?{
        val result = try {
            oneTabClint.beginSignIn(
                beginSiginRequst()
            ).await()
        }catch (e: Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }


    suspend fun signInWithIntent(intent: Intent): Boolean {
        val credential = oneTabClint.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentional = GoogleAuthProvider.getCredential(googleIdToken,null)
        return  try {
            val user  = auth.signInWithCredential(googleCredentional).await().user
            if(user!=null){
                true
            }else{
                false
            }
        }catch (e:Exception){
            e.printStackTrace()
            if (e is CancellationException) throw  e
            false
        }
    }

    suspend fun singOut(){
        try {
            oneTabClint.signOut().await()
            auth.signOut()
        }catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException) throw  e
        }
    }
    /*fun getSiginUser():Userdata? = auth.currentUser?.run {
        Userdata(
            userId = uid,
            userName = displayName,
            desplaiiamge = photoUrl?.toString()
        )
    }*/
    private fun beginSiginRequst(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("919141181344-6m5cb0u5rio39170p35ckmrf62o5kgkv.apps.googleusercontent.com")
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}