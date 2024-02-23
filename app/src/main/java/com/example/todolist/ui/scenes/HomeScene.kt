package com.example.todolist.ui.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.todolist.ui.AppViewModelProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.data.Data
import com.example.todolist.data.DataRepository
import com.example.todolist.ui.theme.BGColor
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.ui.theme.buttonBGColor
import com.example.todolist.ui.theme.cardBGColor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

object HomeDestination{
    val route = "home"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScene(
    navigateToAddData: () -> Unit,
    navigateToDataDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddData,
                shape = CircleShape,
                containerColor = buttonBGColor
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {innerPadding ->
        HomeBody(
            dataList = homeUiState.dataList,
            onDataClick = navigateToDataDetails,
            onAddButtonClick = navigateToAddData,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    dataList: List<Data>,
    onDataClick: (Int) -> Unit,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
){
//    val listHeight = 250.dp
    Column (
        modifier = modifier
            .background(color = BGColor)
            .padding(
                top = 100.dp
            ),
    ){
        Text(
            text = "Todo List",
            color = Color.LightGray,
            modifier = Modifier
                .padding(
                    start = 20.dp
                ),
            fontSize = 30.sp
        )
        DataList(
            modifier = Modifier
//                .height(listHeight)
                .padding(
                    horizontal = 30.dp
                ),
            dataList = dataList,
            cardColor = cardBGColor,
            dataFontSize = 20.sp,
            onDataClick = {onDataClick(it.id)}
        )
//        Text(
//            text = "List",
//            modifier = Modifier
//                .padding(
//                    start = 20.dp
//                ),
//            fontSize = 30.sp
//        )
//        DataList(
//            modifier = Modifier
//                .height(listHeight)
//                .padding(
//                    horizontal = 30.dp
//                ),
//            dataList = dataList,
//            cardColor = Color.Gray,
//            dataFontSize = 20.sp,
//            onDataClick = {onDataClick(it.id)}
//        )
    }
}

@Composable
fun DataList(
    modifier: Modifier = Modifier,
    dataList: List<Data>,
    cardColor: Color,
    dataFontSize: TextUnit,
    onDataClick: (Data) -> Unit,
){
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(items = dataList, key = {it.id}){item->
            Datum(
                datum = item,
                cardColor = cardColor,
                dataFontSize = dataFontSize,
                modifier = Modifier
                    .clickable { onDataClick(item) }
            )
        }
    }
}

@Composable
private fun Datum(
    datum: Data,
    cardColor: Color,
    dataFontSize: TextUnit,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        ),
        modifier = modifier,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = datum.content,
                fontSize = dataFontSize,
                color = Color.Black,
                maxLines = 1,
                modifier = modifier
                    .weight(6f)
                    .padding(8.dp)
            )
            Text(
                text = datum.formatedTime(),
                fontSize = dataFontSize,
                color = Color.Black,
                modifier = modifier
                    .weight(4f)
                    .padding(8.dp)
            )
        }
    }
}

class HomeViewModel(dataRepository: DataRepository) : ViewModel(){
    val homeUiState: StateFlow<HomeUiState> =
        dataRepository.getAlldataStream().map {HomeUiState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    companion object{
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val dataList: List<Data> = listOf())

@Preview(showBackground = true, showSystemUi = true, name = "my app")
@Composable
fun HomeBodyPreview() {
    TodoListTheme {
        HomeBody(listOf(
            Data(1, "Game", 2000, 1, 23, 20000123),
            Data(2, "Pen", 2021, 4, 23, 20210423),
            Data(3, "TV", 2023, 9, 3, 20230903)
        ), onDataClick = {}, onAddButtonClick = {})
    }
}
