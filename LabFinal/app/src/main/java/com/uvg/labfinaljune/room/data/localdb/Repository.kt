package com.uvg.labfinaljune.room.data.localdb


import com.uvg.labfinaljune.ktor.data.network.dto.mapToCryptoAssetModel
import com.uvg.labfinaljune.ktor.domain.network.CryptixApi
import com.uvg.labfinaljune.ktor.domain.network.CryptixRepository
import com.uvg.labfinaljune.ktor.domain.network.util.DataError
import com.uvg.labfinaljune.ktor.domain.network.util.NetworkError
import com.uvg.labfinaljune.room.data.localdb.dao.CryptoAssetDao
import com.uvg.labfinaljune.room.data.localdb.entity.toEntity
import com.uvg.labfinaljune.util.CryptoAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.uvg.labfinaljune.ktor.domain.network.util.Result
import com.uvg.labfinaljune.ktor.domain.network.util.map
import com.uvg.labfinaljune.room.data.localdb.entity.toCryptoAsset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CryptixRepositoryImplementation(
    private val cryptoAssetDao: CryptoAssetDao,
    private val cryptixApi: CryptixApi,
): CryptixRepository {
    override suspend fun getAllCryptoAssets(): Result<List<CryptoAsset>, DataError> {
        when (val result = cryptixApi.getAllCryptoAssets()) {
            is Result.Error -> {
                println(result.error)
                val localCryptoAssets = cryptoAssetDao.getAllCryptoAssets()
                if (localCryptoAssets.isEmpty()) {
                    if (result.error == NetworkError.NO_INTERNET) {
                        return Result.Error(
                            DataError.NO_INTERNET
                        )
                    }
                    return Result.Error(
                        DataError.GENERIC_ERROR
                    )
                } else {
                    return Result.Success(
                        localCryptoAssets.map { it.toCryptoAsset() }
                    )
                }
            }
            is Result.Success -> {
                val remoteCryptoAssets = result.data.data
                return Result.Success(
                    remoteCryptoAssets.map { it.mapToCryptoAssetModel() }
                )
            }
        }
    }

    override suspend fun saveCryptoAssets(): Result<Boolean, DataError> {
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        val remoteCryptoAssets = getAllCryptoAssets()
        if (remoteCryptoAssets is Result.Success) {
            val entities = remoteCryptoAssets.data.map { dto ->
                dto.toEntity().copy(lastModified = currentDate)
            }
            cryptoAssetDao.insertCryptoAssets(
                entities
            )
            return Result.Success(true)
        } else{
            return Result.Error(DataError.GENERIC_ERROR)
        }
    }

    override suspend fun getCryptoAssetById(id: String): Result<CryptoAsset, DataError> {
        println("getting crypto asset by id")
        println(id)
        val remoteCryptoAsset = cryptixApi.getCryptoAsset(id)
        val localCryptoAssets = cryptoAssetDao.getCryptoAssetById(id)
        return when (remoteCryptoAsset) {
            is Result.Success -> {
                Result.Success(remoteCryptoAsset.data.mapToCryptoAssetModel())
            }
            is Result.Error -> {
                if (localCryptoAssets == null) {
                    Result.Error(DataError.GENERIC_ERROR)
                } else {
                    Result.Success(localCryptoAssets.toCryptoAsset())
                }
            }
        }
    }
}