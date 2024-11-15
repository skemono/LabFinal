package com.uvg.labfinaljune.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = CryptoAssetListDestination,
        modifier = modifier
    ){
        cryptoAssetListScreen(
            onCryptoAssetClick = navController::navigateToCryptoAssetProfileScreen,
        )
        cryptoAssetProfileScreen(
            onBack = navController::navigateUp
        )
    }
}