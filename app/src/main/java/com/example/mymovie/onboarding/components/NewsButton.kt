package com.example.mymovie.onboarding.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewsButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {

    Icon(
        imageVector = Icons.Default.ArrowCircleRight,
        contentDescription = "",
        modifier = Modifier
            .clickable { onClick() }
            .size(50.dp),
        tint = MaterialTheme.colorScheme.secondary)
}


@Composable
fun NewsTextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Black
        ), shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text, fontSize = 16.sp,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = LightGray
        )
    }
}



