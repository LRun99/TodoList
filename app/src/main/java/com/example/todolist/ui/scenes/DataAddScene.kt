package com.example.todolist.ui.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.DataRepository
import com.example.todolist.ui.AppViewModelProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import com.example.todolist.data.Data
import java.text.SimpleDateFormat
import java.util.Date

object DataAddDestination{
    val route = "dataAdd"
    const val dataIdArg = "dataId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataAddScene(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DataAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier
    ) {innerPadding ->
        DataAddBody(
            dataUiState = viewModel.dataUiState,
            onDataValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.setTime(viewModel.dataUiState.dataDetails)
                    viewModel.saveData()
                    navigateToHome()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
        )
    }
}

@Composable
fun DataAddBody(
    dataUiState: DataUiState,
    onDataValueChange: (DataDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = Color.DarkGray)
    ){
        SetContent(
            dataDetails = dataUiState.dataDetails,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    30.dp
                )
                .height(
                    150.dp
                )
                .background(color = Color.LightGray),
            onValueChange = onDataValueChange,
        )
        SetDate(
            dataDetails = dataUiState.dataDetails,
            modifier = Modifier
                .height(
                    60.dp
                )
                .weight(
                    0.3f
                )
                .background(color = Color.LightGray),
            onValueChange = onDataValueChange,
        )
        Button(
            onClick = onSaveClick,
            enabled = dataUiState.isEntryValid,
            modifier = Modifier
                .padding(
                    start = 270.dp,
                    top = 30.dp
                )
        ) {
            Text(
                text = "add",
                fontSize = 30.sp,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetContent(
    dataDetails: DataDetails,
    modifier: Modifier = Modifier,
    onValueChange: (DataDetails) -> Unit = {},
    enabled: Boolean = true
){
    OutlinedTextField(
        value = dataDetails.content,
        onValueChange = { onValueChange(dataDetails.copy(content = it)) },
        label = {
            Text(
                text =  "content",
            )
        },
        modifier = modifier
            .padding(
                start = 5.dp
            ),
        enabled = enabled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDate(
    dataDetails: DataDetails,
    modifier: Modifier = Modifier,
    onValueChange: (DataDetails) -> Unit = {},
    enabled: Boolean = true
){
    Row(
        modifier = Modifier
            .padding(
                horizontal = 25.dp
            )
    ){
        OutlinedTextField(
            value = dataDetails.year,
            onValueChange = { onValueChange(dataDetails.copy(year = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1,
            label = {
                Text(
                    text =  "year",
                )
            },
            modifier = modifier
                .padding(
                    start = 5.dp
                ),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = dataDetails.month,
            onValueChange = { onValueChange(dataDetails.copy(month = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1,
            label = {
                Text(
                    text =  "month",
                )
            },
            modifier = modifier
                .padding(
                    start = 5.dp
                ),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = dataDetails.day,
            onValueChange = { onValueChange(dataDetails.copy(day = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1,
            label = {
                Text(
                    text =  "day",
                )
            },
            modifier = modifier
                .padding(
                    start = 5.dp
                ),
            enabled = enabled,
            singleLine = true
        )
    }
}

class DataAddViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var dataUiState by mutableStateOf(DataUiState())
        private set

    fun updateUiState(dataDetails: DataDetails){
        dataUiState = DataUiState(dataDetails = dataDetails, isEntryValid = validateInput(dataDetails))
    }

    private fun validateInput(uiState: DataDetails = dataUiState.dataDetails): Boolean{
        return with(uiState){
            content.isNotBlank() && year.isNotBlank() && month.isNotBlank() && day.isNotBlank()
        }
    }

    fun setTime(uiState: DataDetails = dataUiState.dataDetails){
        uiState.time = (uiState.year.toInt() * 10000 + uiState.year.toInt() * 100 + uiState.day.toInt()).toString()
    }

    suspend fun saveData(){
        if(validateInput()){
            dataRepository.insertData((dataUiState.dataDetails.toData()))
        }
    }
}


data class DataUiState(
    val dataDetails: DataDetails = DataDetails(),
    val isEntryValid: Boolean = false
)

data class DataDetails(
    val id: Int = 0,
    val content: String = "",
    val year: String = "",
    val month: String = "",
    val day: String = "",
    var time: String = "",
)

fun DataDetails.toData(): Data = Data(
    id = id,
    content = content,
    year = year.toIntOrNull() ?: 0,
    month = month.toIntOrNull() ?: 0,
    day = day.toIntOrNull() ?: 0,
    time = time.toIntOrNull() ?: 0,
)

fun Data.formatedYear(): String{
    return NumberFormat.getCurrencyInstance().format(year)
}

fun Data.formatedMonth(): String{
    return NumberFormat.getCurrencyInstance().format(month)
}

fun Data.formatedDay(): String{
    return NumberFormat.getCurrencyInstance().format(day)
}

fun Data.formatedTime(): String{
    return year.toString() + "/" + month.toString() + "/" + day.toString()
}

fun Data.formatedDisplayContent(): String{
    val dataLength: Int = 15
    var displayContent = content
    if(content.length >= dataLength) displayContent = content.substring(0,dataLength) + "..."
    return  displayContent
}

fun Data.toDataUiState(isEntryValid: Boolean = false): DataUiState = DataUiState(
    dataDetails = this.toDataDetails(),
    isEntryValid = isEntryValid
)

fun Data.toDataDetails(): DataDetails = DataDetails(
    id = id,
    content = content,
    year = year.toString(),
    month = month.toString(),
    day = day.toString(),
    time = time.toString(),
)














