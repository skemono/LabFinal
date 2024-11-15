package com.uvg.labfinaljune.ktor.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetListDto(
    val data: List<CryptoAssetDto>
)