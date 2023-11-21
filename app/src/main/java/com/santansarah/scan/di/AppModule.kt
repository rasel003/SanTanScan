package com.santansarah.scan.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.santansarah.scan.domain.interfaces.IAnalytics
import com.santansarah.scan.presentation.BleGatt
import com.santansarah.scan.presentation.BleManager
import com.santansarah.scan.presentation.control.ControlViewModel
import com.santansarah.scan.presentation.scan.ScanViewModel
import com.santansarah.scan.utils.logging.Analytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single<IAnalytics> { Analytics() }

    fun provideBluetoothManager(app: Application): BluetoothManager {
        return app.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    single<BluetoothAdapter> { provideBluetoothManager(androidApplication()).adapter }

    factory(named("IODispatcher")) {
        Dispatchers.IO
    }

    factory { CoroutineScope(get<CoroutineDispatcher>(named("IODispatcher")) + Job()) }

    single { BleManager(get(), get(), get()) }
    single { BleGatt(androidApplication(), get(), get(), get(), get(), get()) }

    viewModel { ScanViewModel(get(), get(), get(), get(named("IODispatcher")), get()) }
    viewModel { ControlViewModel(get(), get(), get(named("IODispatcher")), get()) }

}