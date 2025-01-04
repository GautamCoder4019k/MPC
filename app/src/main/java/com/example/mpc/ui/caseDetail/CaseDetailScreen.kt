package com.example.mpc.ui.caseDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mpc.data.CaseData
import com.example.mpc.data.StudentData
import com.example.mpc.ui.addCase.RecordBox
import com.example.mpc.ui.admin.addStudent.OutlinedTextFieldWithHeading
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue
import com.example.mpc.ui.theme.LightGray

@Composable
fun CaseDetailScreen(
    modifier: Modifier = Modifier,
    id: String = "",
    onCaseClick: (String) -> Unit,
    navigateBack: () -> Unit
) {

    val viewModel: CaseDetailScreenViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.getCaseData(id)
    }
    var selected by rememberSaveable {
        mutableStateOf("case")
    }
    var activePhotos by remember { mutableStateOf("") }
    WaveShape()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(160.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Case",
                fontWeight = FontWeight.Bold,
                color = if (selected == "case") DarkBlue else Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                textDecoration = if (selected == "case") TextDecoration.Underline else TextDecoration.None,
                modifier = Modifier.clickable { selected = "case" }
            )
            Text(
                text = "Student",
                fontWeight = FontWeight.Bold,
                color = if (selected == "student") DarkBlue else Color.Black,
                style = MaterialTheme.typography.headlineMedium,
                textDecoration = if (selected == "student") TextDecoration.Underline else TextDecoration.None,
                modifier = Modifier.clickable { selected = "student" }
            )
        }
        when (selected) {
            "case" -> CaseDetail(
                modifier = Modifier.fillMaxWidth(0.8f),
                caseData = viewModel.caseData.value,
                viewModel = viewModel,
                onImageClick = { activePhotos = it }
            )

            "student" -> StudentDetail(
                modifier = Modifier.fillMaxWidth(0.8f),
                studentData = viewModel.studentData.value,
                pastCases = viewModel.pastCasesData.value,
                currentCaseId = id,
                onCaseClick = onCaseClick
            )
        }
        if (viewModel.isAdmin.value)
            Button(
                enabled = viewModel.caseData.value.status == "Pending",
                onClick = {
                    viewModel.onApproveClick()
                    navigateBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = if (viewModel.caseData.value.status == "Pending") "Approve" else "Approved",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
    }
    if (activePhotos != "") {
        FullScreenImage(
            imageUrl = activePhotos,
            onDismiss = { activePhotos = "" }
        )
    }

}

@Composable
 fun FullScreenImage(
    imageUrl: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scrim(onDismiss, Modifier.fillMaxSize())
        ImageWithZoom(imageUrl, Modifier.aspectRatio(1f))
    }
}

// [START android_compose_touchinput_pointerinput_scrim]
@OptIn(ExperimentalComposeUiApi::class)
@Composable
 fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    val strClose = "close"
    Box(
        modifier
            // handle pointer input
            // [START android_compose_touchinput_pointerinput_scrim_highlight]
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            // [END android_compose_touchinput_pointerinput_scrim_highlight]
            // handle accessibility services
            .semantics(mergeDescendants = true) {
                contentDescription = strClose
                onClick {
                    onClose()
                    true
                }
            }
            // handle physical keyboard input
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose()
                    true
                } else {
                    false
                }
            }
            // draw scrim
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}
// [END android_compose_touchinput_pointerinput_scrim]


@Composable
 fun ImageWithZoom(imageUrl: String, modifier: Modifier = Modifier) {
    // [START android_compose_touchinput_pointerinput_double_tap_zoom]
    var zoomed by remember { mutableStateOf(false) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier
            // [START android_compose_touchinput_pointerinput_double_tap_zoom_highlight]
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        zoomOffset = if (zoomed) Offset.Zero else
                            calculateOffset(tapOffset, size)
                        zoomed = !zoomed
                    }
                )
            }
            // [END android_compose_touchinput_pointerinput_double_tap_zoom_highlight]
            .graphicsLayer {
                scaleX = if (zoomed) 2f else 1f
                scaleY = if (zoomed) 2f else 1f
                translationX = zoomOffset.x
                translationY = zoomOffset.y
            }
    )
    // [END android_compose_touchinput_pointerinput_double_tap_zoom]
}

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    return Offset(offsetX, 0f)
}

@Composable
fun CaseDetail(
    modifier: Modifier = Modifier,
    caseData: CaseData,
    viewModel: CaseDetailScreenViewModel,
    onImageClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            TextWithBackGround(
                title = "Date:",
                value = caseData.date,
                modifier = Modifier.weight(1f)
            )
            TextWithBackGround(
                title = "Time:",
                value = caseData.time,
                modifier = Modifier.weight(1f)
            )
        }
        TextWithBackGround(title = "Course Code:", value = caseData.courseCode)
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(caseData.proofImages) {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .clickable { onImageClick(it) }
                )
            }
        }
        Text(
            text = "Superintendent:",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium,
        )
        TextWithBackGround(title = "Name:", value = caseData.superintendentName)
        TextWithBackGround(title = "Contact No:", value = caseData.superintendentContactNo)
        TextWithBackGround(title = "Department:", value = caseData.superintendentDepartment)
        Text(
            text = "Statements:",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium,
        )
        RecordBox(
            text = "Student",
            modifier = Modifier.fillMaxWidth(),
            onStartPlaying = { viewModel.playAudio(caseData.studentStatement) },
            onStopPlaying = { viewModel.stopAudio() },
            fileName = "    Recorded Audio",
            playUrl = caseData.studentStatement
        )
        OutlinedTextFieldWithHeading(
            text = "Squad Member",
            placeholder = "Enter statement",
            minLines = 3,
            maxLines = 6,
            readOnly = true,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            value = caseData.squadMemberStatement,
            textFieldModifier = Modifier.fillMaxWidth()
        )

    }

}

@Composable
fun StudentDetail(
    modifier: Modifier = Modifier,
    studentData: StudentData,
    pastCases: List<CaseData> = emptyList(),
    currentCaseId: String,
    onCaseClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp, top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextWithBackGround(title = "Usn:", value = studentData.usn)
        TextWithBackGround(title = "Name:", value = studentData.name)
        Row {
            TextWithBackGround(
                title = "Branch:",
                value = studentData.branch,
                modifier = Modifier.weight(1f)
            )
            TextWithBackGround(
                title = "Sem:",
                value = studentData.sem,
                modifier = Modifier.weight(1f)
            )
        }
        TextWithBackGround(title = "Contact No:", value = studentData.contactNo)
        TextWithBackGround(title = "Email:", value = studentData.email)
        TextWithBackGround(title = "Malpractice Count:", value = (pastCases.size).toString())
        Text(
            text = "Past Cases:",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        if (pastCases.size == 1)
            Text(text = "No such case found")
        else
            LazyRow {
                items(pastCases) { caseData ->
//                    Log.d(TAG, "StudentDetail: $it")
                    if (currentCaseId != caseData.id)
                        Card(
                            modifier = Modifier.clickable { onCaseClick(caseData.id) },
                            colors = CardDefaults.cardColors(containerColor = LightGray)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp, bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = caseData.usn, fontWeight = FontWeight.Bold)
                                    Text(text = caseData.name)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = caseData.status,
                                    fontWeight = FontWeight.Bold,
                                    color = Red
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(
                                    containerColor = DarkBlue,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = studentData.sem,
                                        color = White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                                Badge(
                                    containerColor = DarkBlue,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = studentData.branch,
                                        color = White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                                Badge(
                                    containerColor = DarkBlue,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = caseData.courseCode,
                                        color = White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }

                            }

                        }
                }
            }

    }
}

@Composable
fun TextWithBackGround(modifier: Modifier = Modifier, title: String = "", value: String = "") {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(LightGray)
            .padding(4.dp)
    ) {
        Text(text = title, color = DarkBlue, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = value)
    }
}