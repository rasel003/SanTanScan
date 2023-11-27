package com.santansarah.scan.presentation.scan.device

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.santansarah.scan.domain.models.DeviceDetail
import com.santansarah.scan.domain.models.DeviceService
import com.santansarah.scan.presentation.previewparams.PreviewDeviceDetailProvider
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo
import com.santansarah.scan.utils.windowinfo.AppLayoutMode


@Composable
fun ServicePager(
    appLayoutInfo: AppLayoutInfo,
    selectedDevice: DeviceDetail,
    onRead: (String) -> Unit
) {

    val mainBodyModifier = when(appLayoutInfo.appLayoutMode) {
        AppLayoutMode.LANDSCAPE_NORMAL -> Modifier.padding(horizontal = 20.dp)
        AppLayoutMode.LANDSCAPE_BIG -> Modifier.padding(horizontal = 40.dp)
        AppLayoutMode.PORTRAIT_NARROW -> Modifier.padding(horizontal = 16.dp)
        else -> Modifier
    }

    if (selectedDevice.services.isNotEmpty()) {
        Column(
            modifier = mainBodyModifier
        ) {
            val services = selectedDevice.services
            //            var currentServiceIdx by rememberSaveable { mutableStateOf(0) }
            val currentServiceIdx = services.indexOfFirst { it.name.contains("Mfr", true) }.takeIf { it >= 0 } ?: 0


            ServicePagerDetail(
                services[currentServiceIdx],
                onRead
            )
        }
    }
}




@Composable
fun ServicePagerDetail(
    service: DeviceService,
    onRead: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        service.characteristics
            .filter { it.uuid.contains("00001002", ignoreCase = true) }
            .forEach { char ->
            OutlinedCard(
                modifier = Modifier
                    .defaultMinSize(minHeight = 200.dp)
            ) {
                val state by rememberSaveable { mutableStateOf(0) }

                Column(
                    modifier = Modifier.padding(6.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(.85f)
                        ) {

                           /* Text(
                                text = char.name,
                                style = MaterialTheme.typography.titleMedium
                            )*/
                            Text(
                                text = char.uuid.uppercase(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                    }

                    if (state == 0) {
                        ReadCharacteristic(char, onRead)
                    }



                }
            }
            if (service.characteristics.indexOf(char) < service.characteristics.count() - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }


    }

}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFc0e5dd
)
@Composable
fun PreviewServicePager(
    @PreviewParameter(PreviewDeviceDetailProvider::class) deviceDetail: DeviceDetail
) {
    SanTanScanTheme {

        Column {
            ServicePager(
                appLayoutInfo = AppLayoutInfo(
                    appLayoutMode = AppLayoutMode.PORTRAIT,
                    windowDpSize = DpSize(392.dp, 850.dp),
                    foldableInfo = null
                ),
                deviceDetail,
            ) {}
        }
    }
}
