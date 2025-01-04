package com.example.mpc.ui.admin.addSquadMember

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mpc.data.STUDENT_NODE
import com.example.mpc.data.StudentData
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
class AddSquadMemberScreenViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    val isInProgress = mutableStateOf(false)
    private val _uiState = MutableStateFlow(AddSquadMemberScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun addStudent(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        isInProgress.value = true
        val id=fireStore.collection(USER_NODE).document().id
        val user = _uiState.value.toUserData(id)
        auth.createUserWithEmailAndPassword(user.email,"111111")
        fireStore.collection(USER_NODE).document(id).set(user).addOnSuccessListener {
            resetUiState()
            isInProgress.value = false
            onSuccess()
        }
            .addOnFailureListener {
                isInProgress.value = false
                onFailure(it.message.toString())
            }
    }

    private fun resetUiState() {
        _uiState.value = AddSquadMemberScreenUiState()
    }


    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun onDepartmentChange(department: String) {
        _uiState.update {
            it.copy(department = department)
        }
    }

    fun onContactNoChange(contactNo: String) {
        _uiState.update {
            it.copy(contactNo = contactNo)
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun AddSquadMemberScreenUiState.toUserData(id: String): UserData {
        return UserData(
            userId = id,
            name = name,
            department = department,
            phoneNo = contactNo,
            email = email
        )
    }


}

data class AddSquadMemberScreenUiState(
    val name: String = "",
    val department: String = "",
    val contactNo: String = "",
    val email: String = ""
) {
    fun isValid(): Boolean {
        return name.isNotEmpty() && department.isNotEmpty() && contactNo.isNotEmpty() && email.isNotEmpty()
    }
}