package com.uvg.labfinaljune.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.uvg.labfinaljune.CryptoAssetListRoute
import kotlinx.serialization.Serializable

@Serializable
data object CryptoAssetListDestination

fun NavGraphBuilder.cryptoAssetListScreen(
    onCryptoAssetClick: (String) -> Unit,
) {
    composable<CryptoAssetListDestination> {
        CryptoAssetListRoute(
            onCryptoAssetClick = onCryptoAssetClick
        )
    }
}