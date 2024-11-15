package com.uvg.labfinaljune.ktor.domain.network

import com.uvg.labfinaljune.ktor.domain.network.util.DataError
import com.uvg.labfinaljune.util.CryptoAsset
import com.uvg.labfinaljune.ktor.domain.network.util.Result

interface CryptixRepository {
    suspend fun getAllCryptoAssets(): Result<List<CryptoAsset>, DataError>
    suspend fun getCryptoAssetById(id: String): Result<CryptoAsset, DataError>
    suspend fun saveCryptoAssets(): Result<Boolean, DataError>
}