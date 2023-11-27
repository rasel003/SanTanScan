package com.santansarah.scan.presentation.scan.device

import androidx.compose.runtime.Composable
import com.santansarah.scan.local.entities.displayName
import com.santansarah.scan.domain.models.ScanUI
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo

@Composable
fun ShowDeviceBody(
    appLayoutInfo: AppLayoutInfo,
    scanUi: ScanUI,
    onEdit: (Boolean) -> Unit,
    isEditing: Boolean,
    onSave: (String) -> Unit,
    onRead: (String) -> Unit
) {

    val scannedDevice = scanUi.selectedDevice!!.scannedDevice

    if (isEditing)
        EditDevice(onSave = onSave, updateEdit = onEdit, scannedDevice.displayName())
    else {
        ServicePager(
            appLayoutInfo = appLayoutInfo,
            selectedDevice = scanUi.selectedDevice,
            onRead = onRead
        )

    }
}


