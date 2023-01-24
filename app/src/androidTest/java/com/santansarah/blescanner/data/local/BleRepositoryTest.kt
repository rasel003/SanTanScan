package com.santansarah.blescanner.data.local

import com.santansarah.blescanner.data.local.entities.ScannedDevice
import com.santansarah.blescanner.di.testDatabaseModule
import com.santansarah.blescanner.util.AddKoinModules
import com.santansarah.sharedtest.deviceList
import com.santansarah.sharedtest.newDevice
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
internal class BleRepositoryTest: KoinTest {

    @JvmField
    @RegisterExtension
    val extension = AddKoinModules(
        testDatabaseModule
    )

    private val bleDb by inject<BleDatabase>()
    private val bleRepository by inject<BleRepository>()

    @AfterAll
    @Throws(IOException::class)
    fun closeDb() {
        bleDb.close()
    }

    @Test
    @DisplayName("Insert device and read from flow")
    fun insertDevice() = runTest {
        bleRepository.insertDevice(newDevice)
        val scannedDevices = bleRepository.getScannedDevices().first()
        assertEquals(newDevice.address, scannedDevices.first().address)
    }

    @Test
    @DisplayName("Verify all devices are deleted")
    fun deleteScans() = runTest {
        deviceList.forEach {
            bleRepository.insertDevice(it)
        }
        bleRepository.deleteScans()
        val scannedDevices = bleRepository.getScannedDevices().first()
        assertEquals(emptyList<ScannedDevice>(), scannedDevices)
    }

}