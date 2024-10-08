package com.example.recipieapp.sigin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.recipieapp.MainActivity
import com.example.recipieapp.screens.Login_SingUpScreen
import com.example.recipieapp.ui.theme.RecipieAppTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    val googleAuthCalint by lazy { GoogleAuthCalint(applicationContext,Identity.getSignInClient(applicationContext)) }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipieAppTheme {
                var isSigin by remember{ mutableStateOf(false) }
                val  ActivityALauncer = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {result->
                    if(result.resultCode== RESULT_OK){
                        GlobalScope.launch(Dispatchers.IO) {
                            isSigin = googleAuthCalint.signInWithIntent(result.data ?:return@launch)
                        }
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "result code is ${result.resultCode}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                LaunchedEffect (isSigin){
                    if (isSigin){
                        startActivity(Intent(applicationContext,MainActivity::class.java))
                        finish()
                    }
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login_SingUpScreen {
                        GlobalScope.launch {
                            val siginIntentSender = googleAuthCalint.sigIn()
                            ActivityALauncer.launch(
                                IntentSenderRequest.Builder(siginIntentSender ?: return@launch).build()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser!=null){
            startActivity(Intent(application,MainActivity::class.java))
            finish()
        }else{
            return
        }
    }
}

