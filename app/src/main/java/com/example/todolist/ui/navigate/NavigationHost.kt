package com.example.todolist.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import com.example.todolist.ui.scenes.HomeScene
import com.example.todolist.ui.scenes.DataAddScene
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.ui.scenes.DataAddDestination
import com.example.todolist.ui.scenes.DataDetailsDestination
import com.example.todolist.ui.scenes.DataDetailsScene
import com.example.todolist.ui.scenes.DataEditDestination
import com.example.todolist.ui.scenes.DataEditScene
import com.example.todolist.ui.scenes.HomeDestination

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination  = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route){
            HomeScene(
                navigateToAddData = {navController.navigate(DataAddDestination.route)},
                navigateToDataDetails = {navController.navigate("${DataDetailsDestination.route}/${it}")}
            )
        }
        composable(route = DataAddDestination.route){
            DataAddScene(
                navigateToHome = {navController.navigateUp()}
            )
        }
        composable(
            route = DataDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(DataAddDestination.dataIdArg){
                type = NavType.IntType
            }),
        ){
            DataDetailsScene(
                navigateToEditScene = {navController.navigate("${DataEditDestination.route}/$it")},
                navigateToHome = {navController.navigateUp()}
            )
        }
        composable(
            route = DataEditDestination.routeWithArgs,
            arguments = listOf(navArgument(DataAddDestination.dataIdArg){
                type = NavType.IntType
            })
        ){
            DataEditScene(
                navigateToHome = { navController.navigateUp() },
                navigateToDetail = { navController.popBackStack() }
            )
        }
    }
}