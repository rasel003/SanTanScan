package com.santansarah.scan.presentation.scan

import android.content.Context
import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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

@Composable
fun HomeScreen(
    appLayoutInfo: AppLayoutInfo,
    scanState: ScanState,
    onControlClick: (String) -> Unit,
    deviceEvents: DeviceEvents,
    isEditing: Boolean,
    onBackClicked: () -> Unit,
    onSave: (String) -> Unit,
    onShowUserMessage: (String) -> Unit,
    dataFlow: List<ReceivedData>
) {
    val context = LocalContext.current
    ShowDeviceDetail(
        scanState = scanState,
        onBackClicked = onBackClicked,
        onControlClick = onControlClick,
        appLayoutInfo = appLayoutInfo
    )
    Spacer(modifier = Modifier.height(24.dp))

    // Use AndroidView to embed the TextView into Compose
    AndroidView(
        factory = { setupChart( context , getData(dataFlow), colors[0]) },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(16.dp),
        update = {
            // Update the existing view if needed
            //it.text = "Updated Text"
        }
    )
    Text(text = dataFlow.takeLast(15).joinToString { it.value })

    ShowDeviceBody(
        appLayoutInfo = appLayoutInfo,
        scanUi = scanState.scanUI,
        bleReadWriteCommands = scanState.bleReadWriteCommands,
        onShowUserMessage = onShowUserMessage,
        onEdit = deviceEvents.onIsEditing,
        isEditing = isEditing,
        onSave = onSave
    )
}

private val colors = intArrayOf(
    Color.rgb(137, 230, 81),
    Color.rgb(240, 240, 30),
    Color.rgb(89, 199, 250),
    Color.rgb(250, 104, 104)
)
private fun setupChart(context: Context, data: LineData, color: Int) : LineChart {
    val chart = LineChart(context)
    (data.getDataSetByIndex(0) as LineDataSet).circleHoleColor = color

    // no description text
    chart.description.isEnabled = false

    // chart.setDrawHorizontalGrid(false);
    //
    // enable / disable grid background
    chart.setDrawGridBackground(false)
    //        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

    // enable touch gestures
    chart.setTouchEnabled(true)

    // enable scaling and dragging
    chart.isDragEnabled = true
    chart.setScaleEnabled(true)
    chart.setVisibleXRangeMaximum(10F)

    // if disabled, scaling can be done on x- and y-axis separately
    chart.setPinchZoom(false)
    chart.setBackgroundColor(color)

    // set custom chart offsets (automatic offset calculation is hereby disabled)
    chart.setViewPortOffsets(10F, 0F, 10F, 0F)

    // add data
    chart.data = data

    // get the legend (only possible after setting data)
    val l: Legend = chart.legend
    l.isEnabled = false
    chart.axisLeft.isEnabled = false
    chart.axisLeft.spaceTop = 40F
    chart.axisLeft.spaceBottom = 40F
    chart.axisRight.isEnabled = false
    chart.xAxis.isEnabled = false

    // animate calls invalidate()...
    chart.animateX(2500)

    return chart
}

private fun getData(data: List<ReceivedData>): LineData {
    val values = ArrayList<Entry>()
    data.takeLast(35).forEachIndexed { index, receivedData ->
        values.add(Entry(index.toFloat(), receivedData.value.toFloat()*2))

    }
    // create a dataset and give it a type
    val set1 = LineDataSet(values, "DataSet 1")
    // set1.setFillAlpha(110);
    // set1.setFillColor(Color.RED);
    set1.lineWidth = 1.75f
    set1.circleRadius = 5f
    set1.circleHoleRadius = 2.5f
    set1.color = Color.WHITE
    set1.setCircleColor(Color.WHITE)
    set1.highLightColor = Color.WHITE
    set1.setDrawValues(false)

    // create a data object with the data sets
    return LineData(set1)
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
                onShowUserMessage = {},
                onSave = {},
                isEditing = false,
                onBackClicked = {},
                deviceEvents = DeviceEvents({}, {}, {}, {}, {}),
                dataFlow = emptyList()
            )
        }
    }
}


