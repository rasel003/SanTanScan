package com.santansarah.scan.presentation.scan.device

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.santansarah.scan.R
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
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onWrite: (String, String) -> Unit,
    onReadDescriptor: (String, String) -> Unit,
    onWriteDescriptor: (String, String, String) -> Unit
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
                onRead,
                onShowUserMessage,
                onWrite,
                onReadDescriptor,
                onWriteDescriptor
            )
        }
    }
}




@Composable
fun ServicePagerDetail(
    service: DeviceService,
    onRead: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    onWrite: (String, String) -> Unit,
    onReadDescriptor: (String, String) -> Unit,
    onWriteDescriptor: (String, String, String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {

        service.characteristics
//            .filter { it.uuid.contains("E924", ignoreCase = true) }
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

                            Text(
                                text = char.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = char.uuid.uppercase(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    if (state == 0) {
                        ReadCharacteristic(char, onRead, onShowUserMessage)
                    } else {
                        WriteCharacteristic(char, onWrite)
                    }

                    if (char.descriptors.isNotEmpty()) {

                        char.descriptors.forEach { desc ->

                            var descriptorState by rememberSaveable { mutableStateOf(0) }
                            var descriptorExpanded by rememberSaveable { mutableStateOf(false) }

                            Divider(modifier = Modifier.padding(vertical = 10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth(.85f)
                                ) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            modifier = Modifier.size(22.dp),
                                            painter = painterResource(id = R.drawable.descriptor),
                                            contentDescription = "Descriptor Icon",
                                            tint = MaterialTheme.colorScheme.onSecondary
                                        )
                                        Text(
                                            modifier = Modifier.padding(start = 4.dp),
                                            text = desc.name,
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    Text(
                                        //modifier = Modifier.padding(start = 12.dp),
                                        text = desc.uuid.uppercase(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }

                                ReadWriteMenu(
                                    expanded = descriptorExpanded,
                                    onExpanded = { descriptorExpanded = it },
                                    onState = { descriptorState = it })
                            }

                            if (descriptorState == 0) {
                                ReadDescriptor(
                                    char.uuid,
                                    descriptor = desc, onRead = onReadDescriptor,
                                    onShowUserMessage = onShowUserMessage
                                )
                            } else
                                WriteDescriptor(descriptor = desc, onWrite = onWriteDescriptor)

                        }
                        //}
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
                {},
                {},
                { _: String, _: String -> },
                { _: String, _: String -> },
                { _: String, _: String, _: String -> },
            )
        }
    }
}
