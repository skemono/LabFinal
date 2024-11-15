package com.uvg.labfinaljune.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uvg.labfinaljune.ktor.domain.network.util.map
import com.uvg.labfinaljune.ktor.domain.network.util.onError
import com.uvg.labfinaljune.ktor.domain.network.util.onSuccess
import com.uvg.labfinaljune.room.data.localdb.dao.CryptoAssetDao
import com.uvg.labfinaljune.room.data.localdb.di.Dependencies
import com.uvg.labfinaljune.room.data.localdb.entity.toCryptoAsset
import com.uvg.labfinaljune.room.data.localdb.entity.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.labfinaljune.ktor.data.network.KtorCryptixApi
import com.uvg.labfinaljune.ktor.di.KtorDependencies
import com.uvg.labfinaljune.ktor.domain.network.CryptixRepository
import com.uvg.labfinaljune.room.data.localdb.CryptixRepositoryImplementation
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import com.uvg.labfinaljune.util.CryptoAsset


data class CryptoAssetListScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cryptoAssets: List<CryptoAsset> = emptyList()
)

class CryptoAssetListViewModel(
    private val cryptixRepository: CryptixRepository,
    private val cryptoAssetDao: CryptoAssetDao
) : ViewModel() {
    private val _state = MutableStateFlow(CryptoAssetListScreenState())
    val state = _state.asStateFlow()

    init {
        getCryptoAssetsFromAPI()
    }

    fun getCryptoAssetsFromAPI() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            cryptixRepository
                .getAllCryptoAssets()
                .map { response -> response.map { it.toEntity() } }
                .onSuccess { cr ->
                    _state.update {
                        it.copy(
                            cryptoAssets = cr.map { it.toCryptoAsset() })
                    }
                    println("CryptoAssets obtenidos desde la API")
                    _state.update { it.copy(isLoading = false) }
                }
                .onError { error ->
                    val localCryptoAssets = cryptoAssetDao.getAllCryptoAssets()
                    if (localCryptoAssets.isEmpty()) {
                        _state.update { it.copy(error = error.toString()) }
                    } else {
                        _state.update {
                            it.copy(
                                cryptoAssets = localCryptoAssets.map { it.toCryptoAsset() })
                        }
                        println("CryptoAssets obtenidos localmente")
                    }
                    delay(4000)
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val db = Dependencies.provideDatabase(application)
                CryptoAssetListViewModel(
                    cryptixRepository = CryptixRepositoryImplementation(cryptoAssetDao = db.cryptoAssetDao(), cryptixApi = KtorCryptixApi(KtorDependencies.provideHttpClient())),
                    cryptoAssetDao = db.cryptoAssetDao()
                )
            }
        }
    }
}