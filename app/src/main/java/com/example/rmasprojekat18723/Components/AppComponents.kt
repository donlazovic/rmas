package com.example.rmasprojekat18723.Components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.rmasprojekat18723.ui.theme.BgColor
import com.example.rmasprojekat18723.ui.theme.Primary
import com.example.rmasprojekat18723.ui.theme.TextColor
import com.example.rmasprojekat18723.ui.theme.TextFieldColor
import com.google.android.gms.maps.MapView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue


@Composable
fun SignUpTextComponent(value : String , modifier : Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        color = TextColor,
        textAlign = TextAlign.Center
    )

}

@Composable
fun LoginTextComponent(value : String , modifier : Modifier = Modifier){
    Text(
        text = value,
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium,
        fontSize = 46.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        color = TextColor,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    label : String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions : KeyboardOptions,
    leadingIcon: ImageVector? = null,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorStatus : String? = null
) {
    Column(modifier = modifier) {

        OutlinedTextField(
            modifier = modifier,
            label = {
                Text(
                    text = label,
                    color = TextFieldColor
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Primary,
                focusedLabelColor = Primary,
                cursorColor = Primary,
                containerColor = BgColor
            ),
            maxLines = 1,
            singleLine = true,

            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(30),
            leadingIcon = { if (leadingIcon != null) Icon(imageVector = leadingIcon, null) },
            visualTransformation = visualTransformation,
            value = value,
            onValueChange = onValueChange,
            trailingIcon = trailingIcon,
        )
        if (errorStatus != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorStatus,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}



@Composable
fun DontHaveAccount(onSignUpClick: () -> Unit ,modifier: Modifier = Modifier) {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Don't have an account?", color = Color.White , fontSize = 16.sp)
        Spacer(Modifier.width(16.dp))
        TextButton(onClick = onSignUpClick) {
            Text("Register Here" , fontSize = 16.sp)

            }
            
        }

    }

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
            Lifecycle.Event.ON_START -> mapView.onStart()
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> {}
        }
    }

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(timestamp)
}

fun formatDuration(duration: Float): String {
    return String.format("%.1f", duration)
}

@Composable
fun DatePickerField(
    label: String,
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = selectedDate?.let { dateFormat.format(Date(it)) } ?: "Select Date"

    var displayDate by remember { mutableStateOf(formattedDate) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            displayDate = dateFormat.format(calendar.time)
            onDateSelected(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Text(
        text = "$label: $displayDate",
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
            .padding(8.dp)
    )
}

