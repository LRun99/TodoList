package com.example.todolist.ui.scenes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.DataRepository
import com.example.todolist.ui.AppViewModelProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DataEditDestination{
    val route = "dataEdit"
    const val dataIdArg = "dataId"
    val routeWithArgs = "$route/{$dataIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEditScene(
    navigateToHome: () -> Unit,
    navigateToDetail: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DataEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        DataAddBody(
            dataUiState = viewModel.dataUiState,
            onDataValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateData()
                    navigateToHome()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

class DataEditViewModel(
    saveStateHandle: SavedStateHandle,
    private val dataRepository: DataRepository
): ViewModel(){
    var dataUiState by mutableStateOf(DataUiState())
        private set

    private val dataId: Int = checkNotNull(saveStateHandle["dataId"])

    init{
        viewModelScope.launch{
            dataUiState = dataRepository.getDataStream(dataId)
                .filterNotNull()
                .first()
                .toDataUiState(true)
        }
    }

    suspend fun updateData(){
        if(validateInput(dataUiState.dataDetails)){
            dataRepository.updateData(dataUiState.dataDetails.toData())
        }
    }

    fun updateUiState(dataDetails: DataDetails){
        dataUiState = DataUiState(dataDetails = dataDetails, isEntryValid = validateInput(dataDetails))
    }

    private fun validateInput(uiState: DataDetails = dataUiState.dataDetails): Boolean{
        return with(uiState){
            content.isNotBlank() && year.isNotBlank() && month.isNotBlank() && day.isNotBlank()
        }
    }
}

