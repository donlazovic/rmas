package com.example.rmasprojekat18723.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.google.android.gms.maps.model.LatLng
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rmasprojekat18723.Components.rememberMapViewWithLifecycle
import com.example.rmasprojekat18723.data.SignUpViewModel
import com.example.rmasprojekat18723.data.SignUpUIEvent
import com.example.rmasprojekat18723.ui.theme.ButtonColor1
import com.example.rmasprojekat18723.ui.theme.ButtonColor2
import com.google.android.gms.maps.CameraUpdateFactory

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(signOut: () -> Unit, signupViewModel: SignUpViewModel = viewModel()) {
    val context = LocalContext.current
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "HomeScreen",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (hasLocationPermission) {
                val mapView = rememberMapViewWithLifecycle()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    AndroidView(
                        factory = { mapView },
                        modifier = Modifier.fillMaxSize(),
                        update = { map ->
                            map.getMapAsync { googleMap ->
                                googleMap.isMyLocationEnabled = true

                                googleMap.setOnMyLocationChangeListener { location ->
                                    val latLng = LatLng(location.latitude, location.longitude)
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                                }
                            }
                        }
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Location permission is not granted.")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { signupViewModel.onEvent(SignUpUIEvent.SignOutClicked(signOut)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(listOf(ButtonColor1, ButtonColor2)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen({})
}