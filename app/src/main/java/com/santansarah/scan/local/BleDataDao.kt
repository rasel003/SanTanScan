package com.santansarah.scan.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.santansarah.scan.local.entities.BleCharacteristic
import com.santansarah.scan.local.entities.Company
import com.santansarah.scan.local.entities.Descriptor
import com.santansarah.scan.local.entities.MicrosoftDevice
import com.santansarah.scan.local.entities.ReceivedData
import com.santansarah.scan.local.entities.ScannedDevice
import com.santansarah.scan.local.entities.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface BleDataDao {

    @Query("SELECT * FROM received_data order by id limit 50")
    fun getReceivedDataByUuid(): List<ReceivedData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceivedData(device: ReceivedData): Long

    @Query("DELETE from received_data")
    suspend fun deleteReceivedData()

}

