package com.uvg.labfinaljune

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.labfinaljune.architecture.CryptoAssetListScreenState
import com.uvg.labfinaljune.architecture.CryptoAssetListViewModel
import com.uvg.labfinaljune.util.CryptoAsset

@Composable
fun CryptoAssetListRoute(
    onCryptoAssetClick: (String) -> Unit,
    viewModel: CryptoAssetListViewModel = viewModel(factory = CryptoAssetListViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CryptoAssetListScreen(
        state = state,
        onCryptoAssetClick = onCryptoAssetClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAssetListScreen(
    state: CryptoAssetListScreenState,
    onCryptoAssetClick: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "CryptoAssets") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading cryptoAssets...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.9F)
                ) {
                    items(state.cryptoAssets) { cryet ->
                        CryptoAssetRow(cryet = cryet, onClickCrypto = onCryptoAssetClick)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CryptoAssetRow(cryet: CryptoAsset, onClickCrypto: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClickCrypto(cryet.id) }
    ) {
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = cryet.name, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp))
            Row {
                Text(text = "Symbol: ${cryet.symbol}")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Price: ${cryet.priceUsd}")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Change %: ${cryet.changePercent24Hr}",
                    style = TextStyle(
                        color = if (cryet.changePercent24Hr.toFloat() < 1) Color.Red else Color.Green
                    )
                )
            }
        }
    }
}