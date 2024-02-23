package com.example.todolist.ui.scenes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.DataRepository
import com.example.todolist.ui.AppViewModelProvider
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.ui.theme.buttonBGColor
import com.example.todolist.ui.theme.disableButtonBGColor
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
    val dataUiState = viewModel.dataUiState
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("edit content") },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = navigateToDetail
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if(dataUiState.isEntryValid) {
                ExtendedFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.updateTime(viewModel.dataUiState.dataDetails)
                            viewModel.updateData()
                            navigateToHome()
                        }
                    },
                    shape = CircleShape,
                    containerColor = buttonBGColor,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                    )
                    Text(
                        text = "Create"
                    )
                }
            }else{
                ExtendedFloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = disableButtonBGColor,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null
                    )
                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                    )
                    Text(
                        text = "Fill All"
                    )
                }
            }
        }
    ) { innerPadding ->
        DataAddBody(
            dataUiState = viewModel.dataUiState,
            onDataValueChange = viewModel::updateUiState,
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

    fun updateTime(uiState: DataDetails = dataUiState.dataDetails){
        uiState.time = (uiState.year.toInt() * 10000 + uiState.month.toInt() * 100 + uiState.day.toInt()).toString()
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
