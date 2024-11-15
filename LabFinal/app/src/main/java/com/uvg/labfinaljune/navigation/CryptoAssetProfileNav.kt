package com.uvg.labfinaljune.navigation

import com.uvg.labfinaljune.CryptoAssetProfileRoute

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetProfileDestination(val id: String)

fun NavController.navigateToCryptoAssetProfileScreen(
    id: String,
    navOptions: NavOptions? = null
) {
    this.navigate(CryptoAssetProfileDestination(id = id), navOptions)
}

fun NavGraphBuilder.cryptoAssetProfileScreen(
    onBack: () -> Unit,
) {
    composable<CryptoAssetProfileDestination> { backStackEntry ->
        val backStackParams: CryptoAssetProfileDestination = backStackEntry.toRoute()
        CryptoAssetProfileRoute(
            onBack = onBack,
            cryptoAssetId = backStackParams.id
        )
    }
}