package com.santansarah.scan.domain.interfaces

import android.os.ParcelUuid
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Company
import com.santansarah.scan.local.entities.Descriptor
import com.santansarah.scan.local.entities.MicrosoftDevice
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.Service
import com.santansarah.scan.domain.models.ScanFilterOption
import com.santansarah.scan.local.entities.ReceivedData
import kotlinx.coroutines.flow.Flow

interface IBleRepository {

    suspend fun getCompanyById(id: Int): Company?

    suspend fun getServiceById(uuid: String): Service?

    suspend fun getCharacteristicById(uuid: String): BleCharacteristic?

    suspend fun getMicrosoftDeviceById(id: Int): MicrosoftDevice?

    suspend fun insertDevice(device: ScannedDevice): Long

    suspend fun getDeviceByAddress(address: String): ScannedDevice?

    suspend fun deleteScans()

    fun getScannedDevices(scanFilter: ScanFilterOption?): Flow<List<ScannedDevice>>

    suspend fun getMsDevice(
        byteArray: ByteArray
    ): String?

    suspend fun getServices(
        serviceIdRecord: List<ParcelUuid>
    ): List<String>?

    suspend fun getDescriptorById(uuid: String): Descriptor?

    suspend fun updateDevice(scannedDevice: ScannedDevice)

    suspend fun deleteNotSeen()

    // Received Data
    suspend fun getReceivedDataByUuid(uuid: String): Flow<List<ReceivedData>>

    suspend fun insertReceivedData(device: ReceivedData): Long

    suspend fun deleteReceivedData()

}
