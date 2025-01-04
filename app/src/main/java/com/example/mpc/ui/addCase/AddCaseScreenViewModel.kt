package com.example.mpc.ui.addCase

import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpc.data.CASE_NODE
import com.example.mpc.data.CaseData
import com.example.mpc.data.STUDENT_NODE
import com.example.mpc.data.StudentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AddCaseScreenViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        AddCaseScreenUiState(date = LocalDate.now().toString(), time = getLocalTime())
    )

    val uiState = _uiState.asStateFlow()

    val isPermissionGranted = mutableStateOf(false)
    var studentData = mutableStateOf(StudentData())
    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null

    private var mediaPlayer: MediaPlayer? = null

    fun saveCase() {
        val id = firestore.collection(CASE_NODE).document().id
        val caseData = CaseData(
            id = id,
            usn = uiState.value.usn,
            name = uiState.value.name,
            squadMemberId = auth.uid ?: "",
            date = uiState.value.date,
            time = uiState.value.time,
            nature = uiState.value.nature,
            courseCode = uiState.value.courseCode,
            superintendentName = uiState.value.superintendentName,
            superintendentContactNo = uiState.value.superintendentContactNo,
            superintendentDepartment = uiState.value.superintendentDepartment,
            studentStatement = uiState.value.studentStatement,
            squadMemberStatement = uiState.value.squadMemberStatement,
            proofImages = uiState.value.proofImages
        )
        clearData()
        firestore.collection(CASE_NODE).document(id).set(caseData).addOnSuccessListener {

            Log.d(TAG, "saveCase: Success")
        }
            .addOnFailureListener {
                Log.d(TAG, "saveCase: Failure")
            }
    }

    private fun getLocalTime(): String {
        val date = Date()
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getStudentData(usn: String) {
        firestore.collection(STUDENT_NODE).whereEqualTo("usn", usn).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    studentData.value =
                        it.documents[0].toObject(StudentData::class.java) ?: StudentData()
                    Log.d(TAG, "getStudentData: ${studentData.value}")
                    onNameChange(studentData.value.name)
                }
            }
    }

    private fun clearData() {
        _uiState.update {
            AddCaseScreenUiState(date = LocalDate.now().toString(), time = getLocalTime())
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

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }

    fun startRecording() {
        val outputFile = File.createTempFile("audio_record_", ".mp3")
        audioFile = outputFile

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }
    }

    fun stopRecordingAndUpload(type: String = "student") {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        audioFile?.let {
            Log.d(TAG, "stopRecordingAndUpload: ${audioFile!!.absolutePath}")
            uploadAudioToFirebase(type, it)
        }
    }

    fun uploadImagesToFirebase(imageUris: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageUrls = mutableListOf<String>()

            for (uri in imageUris) {
                val imageUrl = uploadImage(uri)
                if (imageUrl != null) {
                    imageUrls.add(imageUrl)
                } else {
                    Log.d(TAG, "uploadImagesToFirebase: failed to get URL for $uri")
                }
            }

            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(proofImages = imageUrls)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun uploadImage(uriString: String): String? =
        suspendCancellableCoroutine { continuation ->
            val storageReference =
                storage.reference.child("image_files/${uriString.toUri().lastPathSegment}")

            storageReference.putFile(uriString.toUri())
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.reference?.downloadUrl
                        ?.addOnSuccessListener { url ->
                            continuation.resume(url.toString(), null)
                        }
                        ?.addOnFailureListener { exception ->
                            Log.d(TAG, "uploadImage: error ${exception.message}")
                            continuation.resume(null, null)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "uploadImage: error ${exception.message}")
                    continuation.resume(null, null)
                }
        }

    private fun uploadAudioToFirebase(type: String, file: File) {
        val storageReference = storage.reference.child("audio_files/${file.name}")
        viewModelScope.launch(Dispatchers.IO) {
            storageReference.putFile(file.toUri()).addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                    when (type) {
                        "student" -> {
                            _uiState.update {
                                it.copy(studentStatement = url.toString())
                            }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(squadMemberStatement = url.toString())
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                // Handle failure
                Log.d(TAG, "uploadAudioToFirebase: error ${it.message}")
            }
        }
    }

    fun onUsnChange(usn: String) {
        _uiState.update {
            it.copy(usn = usn)
        }
        getStudentData(usn)
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(name = name)
        }
    }

    fun onNatureChange(nature: String) {
        _uiState.update {
            it.copy(nature = nature)
        }
    }

    fun onCourseCodeChange(courseCode: String) {
        _uiState.update {
            it.copy(courseCode = courseCode)
        }
    }

    fun onSuperintendentNameChange(superintendentName: String) {
        _uiState.update {
            it.copy(superintendentName = superintendentName)
        }
    }

    fun onSuperintendentContactNoChange(superintendentContactNo: String) {
        _uiState.update {
            it.copy(superintendentContactNo = superintendentContactNo)
        }
    }

    fun onSuperintendentDepartmentChange(superintendentDepartment: String) {
        _uiState.update {
            it.copy(superintendentDepartment = superintendentDepartment)
        }
    }

    fun onSquadMemberStatementChange(squadMemberStatement: String) {
        _uiState.update {
            it.copy(squadMemberStatement = squadMemberStatement)
        }
    }
}

data class AddCaseScreenUiState(
    val usn: String = "",
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val nature: String = "",
    val courseCode: String = "",
    val superintendentName: String = "",
    val superintendentContactNo: String = "",
    val superintendentDepartment: String = "",
    val studentStatement: String = "",
    val squadMemberStatement: String = "",
    val proofImages: List<String> = emptyList()
) {
    fun isDataValid(): Boolean {
        return usn.isNotEmpty() && name.isNotEmpty() && nature.isNotEmpty() && courseCode.isNotEmpty()
    }

    fun isRemainingDataValid(): Boolean {
        return studentStatement.isNotEmpty() && squadMemberStatement.isNotEmpty() && superintendentName.isNotEmpty() && superintendentContactNo.isNotEmpty() && superintendentDepartment.isNotEmpty()
    }

}