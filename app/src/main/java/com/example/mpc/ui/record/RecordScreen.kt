package com.example.mpc.ui.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mpc.data.CaseData
import com.example.mpc.ui.components.WaveShape
import com.example.mpc.ui.theme.DarkBlue
import com.example.mpc.ui.theme.LightGray
import com.example.mpc.ui.theme.LightGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    modifier: Modifier = Modifier,
    onCaseClick: (String) -> Unit,
    navigateToFilter: () -> Unit,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val caseList = viewModel.caseList
    val filteredList = caseList.value.filter {
        (viewModel.selectedBranches.isEmpty() || it.courseCode in viewModel.selectedBranches) &&
                (viewModel.selectedSems.isEmpty() || it.nature in viewModel.selectedSems) &&
                (viewModel.selectedStatuses.isEmpty() || it.status in viewModel.selectedStatuses)
    }

    WaveShape()
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(160.dp))
        Text(
            text = "Records",
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            style = MaterialTheme.typography.headlineMedium
        )
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
        }
        Button(
            onClick = { navigateToFilter() }, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.FilterAlt, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Filter")
        }
        LazyColumn {
            items(filteredList) {
                CaseCard(it,
                    Modifier
                        .padding(16.dp)
                        .clickable {
                            onCaseClick(it.id)
                            viewModel.getCases()
                        })
            }
        }

    }
}

@Composable
fun CaseCard(caseData: CaseData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = caseData.usn, fontWeight = FontWeight.Bold)
                Text(text = caseData.name)
            }
            Text(
                text = caseData.status,
                fontWeight = FontWeight.Bold,
                color = if (caseData.status == "Pending") Red else LightGreen
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Badge(containerColor = DarkBlue, modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = "IV Sem",
                    color = White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Badge(containerColor = DarkBlue, modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = "CSE",
                    color = White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Badge(containerColor = DarkBlue, modifier = Modifier.padding(end = 8.dp)) {
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