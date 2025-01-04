package com.example.mpc.ui.record

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mpc.data.CASE_NODE
import com.example.mpc.data.CaseData
import com.example.mpc.data.STUDENT_NODE
import com.example.mpc.data.StudentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    @SuppressLint("MutableCollectionMutableState")
    var caseList = mutableStateOf(mutableListOf<CaseData>())
        private set

    var selectedSems by mutableStateOf(emptySet<String>())
    var selectedBranches by mutableStateOf(emptySet<String>())
    var selectedStatuses by mutableStateOf(emptySet<String>())

    init {
        getCases()
    }

     fun getCases() {
        if (auth.currentUser?.email == "t@gmail.com") {
            firestore.collection(CASE_NODE).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        caseList.value = it.toObjects(CaseData::class.java)
                        Log.d(TAG, "getCases: success $caseList")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "getCases: failure $it")
                }
        } else
            firestore.collection(CASE_NODE)
                .whereEqualTo("squadMemberId", auth.currentUser?.uid ?: "")
                .get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        caseList.value = it.documents.map {
                            it.toObject(CaseData::class.java)!!
                        }.toMutableList()
                        Log.d(TAG, "getCases: success $caseList")
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "getCases: failure $it")
                }
    }

}