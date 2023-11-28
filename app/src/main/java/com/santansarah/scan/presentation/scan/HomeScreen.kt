package com.santansarah.scan.presentation.scan

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.santansarah.scan.domain.models.DeviceEvents
import com.santansarah.scan.domain.models.ScanState
import com.santansarah.scan.local.entities.ReceivedData
import com.santansarah.scan.presentation.previewparams.DevicePortraitParams
import com.santansarah.scan.presentation.previewparams.FeatureParams
import com.santansarah.scan.presentation.previewparams.PortraitLayouts
import com.santansarah.scan.presentation.scan.device.ShowDeviceBody
import com.santansarah.scan.presentation.scan.device.ShowDeviceDetail
import com.santansarah.scan.presentation.theme.SanTanScanTheme
import com.santansarah.scan.utils.windowinfo.AppLayoutInfo
import kotlin.random.Random

@Composable
fun HomeScreen(
    appLayoutInfo: AppLayoutInfo,
    scanState: ScanState,
    onControlClick: (String) -> Unit,
    deviceEvents: DeviceEvents,
    isEditing: Boolean,
    onBackClicked: () -> Unit,
    onSave: (String) -> Unit,
    dataFlow: List<ReceivedData>
) {
    val context = LocalContext.current
    var dataPoints by remember { mutableStateOf(generateDataPoints()) }
    var count by remember { mutableStateOf(dataPoints.size) }

    var lineChart by remember { mutableStateOf<LineChart?>(null) }

    ShowDeviceDetail(
        scanState = scanState,
        onBackClicked = onBackClicked,
        onControlClick = onControlClick,
        appLayoutInfo = appLayoutInfo
    )
    Spacer(modifier = Modifier.height(24.dp))


    Text(text = dataFlow.takeLast(15).joinToString { it.value })

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Set the height to 200dp
        ) {
            AndroidView(
                factory = {
                    LineChart(it).apply {
                        lineChart = this
                        setNoDataText("Loading...") // Set an empty string to avoid the "Loading..." message

                        // Add this block to set initial data
                        val entries = dataPoints.mapIndexed { index, dataPoint ->
                            Entry(index.toFloat(), dataPoint.y)
                        }

                        // Customize X-axis
                        val xAxis: XAxis = xAxis
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.setDrawGridLines(false)
                        xAxis.labelRotationAngle = -45f // Optional: Rotate labels for better visibility

                        // Customize Y-axis
                        axisLeft.setDrawGridLines(false)
                        axisRight.setDrawGridLines(false)

                        // Enable drag and scaling on the x-axis
                        isDragEnabled = true
                        setScaleXEnabled(true)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp),
                update = {
                    // Update the chart if needed
                    lineChart?.apply {

                        //data.getDataSetByIndex(0).clear()
                        val entries = dataPoints.mapIndexed { index, dataPoint ->
                            Entry(index.toFloat(), dataPoint.y)
                        }
                        val maxYValue = 30f  // Set your desired maximum y-value here

                        val dataSet = LineDataSet(entries, "Label")
                        dataSet.color = Color.BLUE
                        dataSet.valueTextColor = Color.BLACK

                        val lineData = LineData(dataSet)
                        data = lineData

                        // Set an initial visible range on the x-axis
                        val visibleRange = 20f
                        setVisibleXRangeMaximum(visibleRange)
                        setVisibleXRangeMinimum(visibleRange)

                        // Set a maximum value on the y-axis label
                        axisLeft.axisMaximum = maxYValue

                        invalidate()
                        moveViewToX(count.toFloat())
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow {
            items(dataPoints) { dataPoint ->
                Text(
                    text = "X: ${dataPoint.x}, Y: ${dataPoint.y}",
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val randomValue = java.util.Random().nextInt(10) + 20.toFloat() // Example: Random data between 10 and 30
                val point = DataPoint(count.toFloat(), randomValue)
                dataPoints += point
                count++
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Random Data Point (Max 30)")
        }
    }

    ShowDeviceBody(
        appLayoutInfo = appLayoutInfo,
        scanUi = scanState.scanUI,
        onEdit = deviceEvents.onIsEditing,
        isEditing = isEditing,
        onSave = onSave
    ) {
        it.toFloatOrNull()?.let {value ->
            val point = DataPoint(count.toFloat(), value)
            dataPoints += point
            count++
        }
    }
}

data class DataPoint(val x: Float, val y: Float)


fun generateDataPoints(count: Int = 1): List<DataPoint> {
    return List(count) {
        val randomValue = java.util.Random().nextInt(10) + 20.toFloat() // Example: Random data between 10 and 30

        DataPoint(it.toFloat(), randomValue)
    }
}

@PortraitLayouts
@Composable
fun PreviewHomeScreen(
    @PreviewParameter(DevicePortraitParams::class) featureParams: FeatureParams
) {
    SanTanScanTheme {
        Column {
            HomeScreen(
                appLayoutInfo = featureParams.appLayoutInfo,
                scanState = featureParams.scanState,
                onControlClick = {},
                deviceEvents = DeviceEvents({}, {}, {}, {}, {}),
                isEditing = false,
                onBackClicked = {},
                onSave = {},
                dataFlow = emptyList()
            )
        }
    }
}


