package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.EventViewModel
import com.example.todolist.ui.theme.TodoListTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

enum class Scenes(){
    Main,
    AddContent,
}

var scene: Int = 1
val todayYear: String = "2023"
val todayMonth: String = "12"
val todayDay: String = "30"

data class Data(
    var content: String = "",
    var year: String = "2024",
    var month: String = "1",
    var day: String = "1",
    var time: String = (year.toInt() * 10000 + month.toInt() * 100 + day.toInt()).toString(),
    var displayContent: String = ""
)

private val _uiState = MutableStateFlow(Data())
val uiState: StateFlow<Data> = _uiState.asStateFlow()

fun makingEventData(): Array<Data>{
    return arrayOf<Data>(
        Data("programming", "2024", "8", "1"),
        Data("job hunt", "2023", "11", "3"),
        Data("make app", "2023", "4", "23"),
        Data("publish entry sheet", "2024", "2", "14")
    )
}

fun makingTodoData(): Array<Data>{
    return arrayOf<Data>(
        Data("publish my PR", "2024", "1", "14"),
        Data("atcoder", "2023", "4", "16"),
        Data("application information technology", "2024", "6", "23"),
        Data("home back", "2024", "2", "21"),
        Data("create android app", "2024", "6", "1"),
        Data("circle develop", "2024", "8", "3")
    )
}

val sampleEventData = makingEventData()
val sampleTodoData = makingTodoData()

fun setDisplayContent(inputData: Array<Data>){
    val dataLength: Int = 20
    for(data in inputData){
        if(data.content.length >= dataLength) data.displayContent = data.content.substring(0,dataLength) + "..."
        else data.displayContent = data.content
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContent(
    modifier: Modifier = Modifier,
    data: String
){
    var input by remember{ mutableStateOf( value = data ) }
    TextField(
        value = input,
        onValueChange = { input = it },
        maxLines = 5,
        label = {
            Text(
                text = "input content",
                )
                },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDate(
    modifier: Modifier = Modifier,
    data: String,
    displayString: String
){
    var input by remember{ mutableStateOf( value = data ) }
    TextField(
        value = input,
        onValueChange = { input = it },
        maxLines = 1,
        label = {
            Text(
                text =  displayString,
                )
                },
        modifier = modifier
            .padding(
                start = 5.dp
            ),
    )
}

@Composable
fun setDate(
    modifier: Modifier = Modifier,
    data: Data
){
    Row(
        modifier = Modifier
            .padding(
                horizontal = 25.dp
            )
    ){
        EditDate(
            modifier = modifier,
            data = data.year,
            displayString = "year"
        )
        EditDate(
            modifier = modifier,
            data = data.month,
            displayString = "month"
        )
        EditDate(
            modifier = modifier,
            data = data.day,
            displayString = "day"
        )
    }
}

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    moveScene: () -> Unit,
){
    Button(
        onClick = { moveScene() },
        modifier = modifier
    ) {
        Text(
            text = "+",
            fontSize = 40.sp
        )
    }

}

fun initializeData(): Data{
    return Data("a", todayYear, todayMonth, todayDay)
}

@Composable
fun CreateLazyColumn(
    modifier: Modifier = Modifier,
    inputData: Array<Data>,
    cardColor: Color
){
    val eventFontSize = 20.sp
    val todoFontSize = 20.sp
    LazyColumn(
        modifier = modifier
    ) {
        for (data in inputData) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor,
                    ),
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = data.displayContent,
                            fontSize = eventFontSize
                        )
                        Text(
                            text = data.time.toString(),
                            fontSize = eventFontSize,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScene(
    modifier: Modifier = Modifier,
    moveToAddScene: () -> Unit,
){
    val listHeight = 250.dp
    Column (
        modifier = Modifier
            .background(color = Color.DarkGray)
            .padding(
                top = 100.dp
            ),
    ){
        Text(
            text = "Event List",
            modifier = Modifier
                .padding(
                    start = 20.dp
                ),
            fontSize = 30.sp
        )
        CreateLazyColumn(
            modifier = Modifier
                .height(listHeight)
                .padding(
                    horizontal = 30.dp
                ),
            inputData = sampleEventData,
            cardColor = Color.Gray
        )
        Text(
            text = "Todo List",
            modifier = Modifier
                .padding(
                    start = 20.dp
                ),
            fontSize = 30.sp
        )
        CreateLazyColumn(
            modifier = Modifier
                .height(listHeight)
                .padding(
                    horizontal = 30.dp
                ),
            inputData = sampleTodoData,
            cardColor = Color.Gray
        )
        AddButton(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    start = 300.dp
                ),
            moveScene = moveToAddScene
        )
    }
}

@Composable
fun EventOrNot(
    modifier: Modifier = Modifier
){
    var checked by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .padding(
                start = 50.dp,
                top = 10.dp
            )
    ) {
        Text(
            text = "check if this content is event",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { newCheckedState ->
                checked = newCheckedState
            },
        )
    }
}

@Composable
fun AddScene(
    modifier: Modifier = Modifier,
    moveToMainScene: () -> Unit,
){
    val data: Data = initializeData()
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = Color.DarkGray)
        ){
        EditContent(
            data = data.content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    30.dp
                )
                .height(
                    150.dp
                )
                .background(color = Color.LightGray)
        )
        setDate(
            data = data,
            modifier = Modifier
                .height(
                    60.dp
                )
                .weight(
                    0.3f
                )
                .background(color = Color.LightGray)
        )
        EventOrNot(
            modifier = Modifier
        )
        Button(
            onClick = { moveToMainScene() },
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

@Preview(showBackground = true, showSystemUi = true, name = "my app")
@Composable
fun MyApp(
    viewModel: EventViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Scenes.Main.name,
    ){
        composable(route = Scenes.Main.name){
            MainScene(
                moveToAddScene = {
                    navController.navigate(Scenes.AddContent.name)
                }
            )
        }
        composable(route = Scenes.AddContent.name){
            AddScene(
                moveToMainScene = {
                    navController.popBackStack(Scenes.Main.name, inclusive = false)
                }
            )
        }
    }
}

















