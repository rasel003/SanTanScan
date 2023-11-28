package com.santansarah.scan.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BackIcon(
    modifier: Modifier = Modifier,
    contentDesc: String
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        tint = Color.Black, // Set your desired color here
        contentDescription = contentDesc
    )
}