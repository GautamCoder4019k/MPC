package com.example.mpc.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mpc.data.USER_NODE
import com.example.mpc.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : ViewModel() {
    var userData: UserData? = null
    val inProgress = mutableStateOf(false)
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState = _uiState.asStateFlow()
    val isAdmin= mutableStateOf(true)

    fun loginWithEmail(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        inProgress.value = true
        auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnSuccessListener {
                fireStore.collection(USER_NODE).document(auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        userData = it.toObject(UserData::class.java)
                        inProgress.value = false
                        onSuccess()
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "loginWithEmail: failed at searching data")
                        inProgress.value = false
                        onFailure(it.message.toString())
                    }
            }
            .addOnFailureListener {
                Log.d(TAG, "loginWithEmail: failed at login")
                inProgress.value = false
                onFailure(it.message.toString())
            }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }
}

data class LoginScreenUiState(
    val email: String = "",
    val password: String = ""
)