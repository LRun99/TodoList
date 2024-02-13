package com.example.todolist.ui.scenes

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.Data
import com.example.todolist.data.DataRepository
import com.example.todolist.ui.AppViewModelProvider
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

object DataDetailsDestination {
    val route = "dataDetail"
    const val dataIdArg = "dataId"
    val routeWithArgs = "$route/{$dataIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataDetailsScene(
    navigateToEditScene: (Int) -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DataDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier
    ) {innerPadding ->
        DataDetailsBody(
            dataDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteData()
                    navigateToHome()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
        )

    }
}

@Composable
private fun DataDetailsBody(
    dataDetailsUiState: DataDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = Modifier
            .padding(16.dp)
    ){
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        DataDetails(
            data = dataDetailsUiState.dataDetails.toData(),
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedButton(
            onClick = {deleteConfirmationRequired = true},
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Text(
                text = "delete"
            )
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun DataDetails(
    data: Data,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DataDetailComponent(
                label = "content",
                dataDetail = data.content,
                modifier = Modifier
                    .padding(16.dp)
            )
            DataDetailComponent(
                label = "date",
                dataDetail = data.formatedTime(),
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun DataDetailComponent(
    label: String,
    dataDetail: String,
    modifier: Modifier = Modifier
){
    Row(modifier = modifier){
        Text(text = label)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = dataDetail)
    }
}
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("delete?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        },
    )
}


class DataDetailsViewModel(
    saveStateHandle: SavedStateHandle,
    private val dataRepository: DataRepository,
): ViewModel(){
    private val dataId: Int = checkNotNull(saveStateHandle[DataDetailsDestination.dataIdArg])

    val uiState: StateFlow<DataDetailsUiState> =
        dataRepository.getDataStream(dataId)
            .filterNotNull()
            .map{
                DataDetailsUiState(dataDetails = it.toDataDetails())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DataDetailsUiState()
            )

    suspend fun deleteData(){
        dataRepository.deleteData((uiState.value.dataDetails.toData()))
    }
    companion object{
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DataDetailsUiState(
    val outOfStock: Boolean = true,
    val dataDetails: DataDetails = DataDetails()
)


@Preview(showBackground = true, showSystemUi = true, name = "my app")
@Composable
fun ItemDetailsScreenPreview() {
    TodoListTheme {
        DataDetailsBody(DataDetailsUiState(
            outOfStock = true, dataDetails = DataDetails(1, "cotnent", "2024","10","12")
        ), onDelete = {})
    }
}
