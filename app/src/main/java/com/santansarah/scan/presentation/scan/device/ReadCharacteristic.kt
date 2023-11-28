package com.santansarah.scan.presentation.scan.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.santansarah.scan.domain.bleparsables.Appearance
import com.santansarah.scan.domain.models.DeviceCharacteristics
import com.santansarah.scan.domain.models.getReadInfo
import com.santansarah.scan.presentation.theme.codeFont
import timber.log.Timber

@Composable
fun ReadCharacteristic(
    char: DeviceCharacteristics,
    onRead: (String) -> Unit
) {

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(6.dp)
    ) {
        SelectionContainer {
            Column {
                onRead(char.getReadInfo())
                Text(
                    text = "Sensor Value : "+char.getReadInfo(),
                    style = codeFont
                )
            }
        }
    }
}
