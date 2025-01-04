package com.example.mpc.ui.admin.addStudent

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mpc.data.STUDENT_NODE
import com.example.mpc.data.StudentData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddStudentScreenViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    val isInProgress = mutableStateOf(false)
    private val _uiState = MutableStateFlow(AddStudentScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun addStudent(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        isInProgress.value = true
        val student = _uiState.value.toStudentData()
        fireStore.collection(STUDENT_NODE).document().set(student).addOnSuccessListener {
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
        _uiState.update {
            it.copy(name = "", usn = "", contactNo = "", email = "")
        }
    }

    fun onUsnChange(usn: String) {
        _uiState.update {
            it.copy(usn = usn)
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun onBranchChange(branch: String) {
        _uiState.update {
            it.copy(branch = branch)
        }
    }

    fun onSemChange(sem: String) {
        _uiState.update {
            it.copy(sem = sem)
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

    fun AddStudentScreenUiState.toStudentData(): StudentData {
        return StudentData(
            usn = usn,
            name = name,
            branch = branch,
            sem = sem,
            contactNo = contactNo,
            email = email
        )
    }



}

data class AddStudentScreenUiState(
    val usn: String = "",
    val name: String = "",
    val branch: String = "",
    val sem: String = "",
    val contactNo: String = "",
    val email: String = ""
) {
    fun isValid(): Boolean {
        return usn.isNotEmpty() && name.isNotEmpty() && branch.isNotEmpty() && sem.isNotEmpty() && contactNo.isNotEmpty() && email.isNotEmpty()
    }
}