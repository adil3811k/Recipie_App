package com.example.recipieapp.newtwork

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait

private val TAG = "FirebaseFireStore"
private val UID = FirebaseAuth.getInstance().currentUser?.uid
private val REFRENCE = Firebase.firestore.collection(UID!!).document("Favorites")
class FirebaseService {
    suspend fun isFavorites(id:String):Boolean{
        val result =  REFRENCE.get().await()
        if (result.exists()){
            Log.d(TAG, "Reference exist")
            val list = result.get("favorites") as?  MutableList<String> ?: mutableListOf()
            Log.d(TAG, "list is $list")
            if (list.contains(id)){
                return true
            }else{
                return false
            }
        }else{
            return false
        }
    }

    suspend fun TogalFavorets(id:String){
        val result =  REFRENCE.get().await()
        if (result.exists()){
            val list = result.get("favorites") as? MutableList<String> ?: mutableListOf()
            if (list.contains(id)){
                // TODO: remove item
                remove(list, id)
            }else{
                // TODO: add item
                add(list,id)
            }
        }else{
            val list = listOf(id)
            val map:HashMap<String ,Any> = hashMapOf("favorites" to list)
            REFRENCE.set(map)
        }
    }
     suspend fun remove(list: MutableList<String>, id: String){
         list.remove(id)
         REFRENCE.update(hashMapOf<String , Any>("favorites" to list)).await()
    }
    suspend fun add(list: MutableList<String>, id: String){
        list.add(id)
        REFRENCE.set(hashMapOf<String , Any>("favorites" to list)).await()
    }
}