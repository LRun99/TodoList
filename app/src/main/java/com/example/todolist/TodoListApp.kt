package com.example.todolist

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.navigate.NavigationHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListApp(navController: NavHostController = rememberNavController()){
    NavigationHost(navController = navController)
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopAppBar(
//    title: String,
//    canNavicateBack: Boolean,
//    modifier: Modifier = Modifier,
//    scrollBehavior: TopAppBarScrollBehavior? = null,
//    navigateUp: () -> Unit = {}
//)
