package com.example.todolist.ui.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.todolist.ui.theme.BGColor
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.ui.theme.buttonBGColor
import com.example.todolist.ui.theme.cardBGColor
import com.example.todolist.ui.theme.disableButtonBGColor
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
    val dataUiState = viewModel.dataUiState
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("add content") },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = navigateToHome
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
                                viewModel.setTime(viewModel.dataUiState.dataDetails)
                                viewModel.saveData()
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
    ) {innerPadding ->
        DataAddBody(
            dataUiState = viewModel.dataUiState,
            onDataValueChange = viewModel::updateUiState,
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
    modifier: Modifier = Modifier,
){
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = BGColor
            )
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
                ),
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
                ),
            onValueChange = onDataValueChange,
        )
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 45.dp
                )
                .padding(
                    top = 5.dp
                )
        ) {
            Spacer(
                modifier = Modifier
                    .weight(3f)
            )
            SetDataType(
                dataDetails = dataUiState.dataDetails,
                modifier = Modifier
                    .weight(1f),
                onValueChange = onDataValueChange
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
    TextField(
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
        colors = TextFieldDefaults.textFieldColors(
            containerColor = cardBGColor,
        )
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
        TextField(
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
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = cardBGColor,
            )
        )

        TextField(
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
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = cardBGColor,
            )
        )

        TextField(
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
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = cardBGColor,
            )
        )
    }
}

@Composable
fun SetDataType(
    dataDetails: DataDetails,
    modifier: Modifier = Modifier,
    onValueChange: (DataDetails) -> Unit = {},
){
    var state by remember{ mutableStateOf(true) }
    Column(
        modifier = Modifier
    ){
        Row(
            modifier = Modifier
        ) {
            RadioButton(
                selected = state,
                onClick = {
                    onValueChange(dataDetails.copy(type = 1))
                    state = true
                },
            )
            Text(
                text = "todo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        top = 15.dp
                    )
            )
        }
        Row(
                modifier = Modifier
        ) {
            RadioButton(
                selected = !state,
                onClick = {
                    onValueChange(dataDetails.copy(type = 2))
                    state = false
                }
            )
            Text(
                text = "event",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(
                        top = 15.dp
                    )
            )
        }
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
        uiState.time = (uiState.year.toInt() * 10000 + uiState.month.toInt() * 100 + uiState.day.toInt()).toString()
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
    val type: Int = 1,
)

fun DataDetails.toData(): Data = Data(
    id = id,
    content = content,
    year = year.toIntOrNull() ?: 0,
    month = month.toIntOrNull() ?: 0,
    day = day.toIntOrNull() ?: 0,
    time = time.toIntOrNull() ?: 0,
    type = type
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

fun Data.displayType(): String{
    if(type == 1) return("todo")
    else return("event")
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
    type = type
)

@Preview(showBackground = true, showSystemUi = true, name = "my app")
@Composable
fun DataAddScenePreview() {
    TodoListTheme {
        DataAddBody(
            dataUiState = DataUiState(DataDetails(1, "Game", "2000", "1", "23", "20000123", 1)),
            onDataValueChange = {})
    }
}












