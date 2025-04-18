package com.example.myapplication.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.*


@Composable
fun TimePickerField(
    label: String,
    time: String,
    onTimeSelected: (String) -> Unit,
    context: Context = LocalContext.current,
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(formattedTime)
                showDialog.value = false
            },
            hour,
            minute,
            true
        ).show()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Log.d("TimePicker", "Clic detectado desde Box")
                showDialog.value = true
            }
    ) {
        OutlinedTextField(
            value = time,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            enabled = false // ✅ debe estar habilitado
        )
    }

}
