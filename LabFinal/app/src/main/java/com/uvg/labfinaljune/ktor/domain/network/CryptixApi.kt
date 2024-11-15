package com.uvg.labfinaljune.ktor.domain.network

import com.uvg.labfinaljune.ktor.data.network.dto.CryptoAssetDto
import com.uvg.labfinaljune.ktor.data.network.dto.CryptoAssetEntryDto
import com.uvg.labfinaljune.ktor.data.network.dto.CryptoAssetListDto
import com.uvg.labfinaljune.ktor.domain.network.util.NetworkError
import com.uvg.labfinaljune.ktor.domain.network.util.Result

interface CryptixApi {
    suspend fun getAllCryptoAssets(): Result<CryptoAssetListDto, NetworkError>
    suspend fun getCryptoAsset(id: String): Result<CryptoAssetEntryDto, NetworkError>
}