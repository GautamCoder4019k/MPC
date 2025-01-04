package com.example.mpc.ui.caseDetail

import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpc.data.CASE_NODE
import com.example.mpc.data.CaseData
import com.example.mpc.data.STUDENT_NODE
import com.example.mpc.data.StudentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CaseDetailScreenViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) :
    ViewModel() {
    private var mediaPlayer: MediaPlayer? = null
    val isAdmin = mutableStateOf(auth.currentUser?.email == "t@gmail.com")
    var caseData = mutableStateOf(CaseData())
    var pastCasesData = mutableStateOf(listOf(CaseData()))
    var studentData = mutableStateOf(StudentData())

    fun getCaseData(id: String) {
        firestore.collection(CASE_NODE).whereEqualTo("id", id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    caseData.value =
                        it.documents[0].toObject(CaseData::class.java) ?: CaseData()
                    getStudentData(caseData.value.usn)
                    getPastCasesData(caseData.value.usn)
                }
            }
    }

    private fun getStudentData(usn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection(STUDENT_NODE).whereEqualTo("usn", usn).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        studentData.value =
                            it.documents[0].toObject(StudentData::class.java) ?: StudentData()
                        Log.d(TAG, "getStudentData: ${studentData.value}")
                    }
                }
        }

    }

    private fun getPastCasesData(usn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firestore.collection(CASE_NODE).whereEqualTo("usn", usn).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        pastCasesData.value = it.toObjects(CaseData::class.java)
                        Log.d(TAG, "getPastCasesData: ${pastCasesData.value}")
                    }
                }
        }
    }

    fun playAudio(url: String) {
        stopAudio() // Stop any currently playing audio

        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                prepare() // Might take long! (for buffering, etc)
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }

    fun onApproveClick() {
        val documentRef = firestore.collection(CASE_NODE).document(caseData.value.id)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                documentRef.update("status", "Approved").await()
                withContext(Dispatchers.Main) {
                    Log.d("Firestore", "Status updated successfully")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Firestore", "Error updating status: ${e.message}")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}