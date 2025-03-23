package com.example.location

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.location.ui.theme.LocationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationTheme {
              myapp(viewModel)
            }
        }
    }
}

@Composable
fun myapp(viewModel: LocationViewModel ){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(locationUtils,viewModel,context)
}

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
){

    val location = viewModel.location.value

    val address = location?.let{
        locationUtils.reverseGeocodeLocation(location)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
    onResult ={
        permissions->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true
            && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true){
            // Location permission granted

            locationUtils.requestLocationUpdates(viewModel=viewModel)
        }else{
            // ask for permission
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )||ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (rationaleRequired){
                Toast.makeText(context,"Location is Required", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Please Enable the Loction", Toast.LENGTH_LONG).show()
            }
        }
    }
)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(location!=null){
            Text("Address: ${location.latitude}, ${location.longitude} \n $address")
        }else{
            Text("Location Not Avialable ")
        }
        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)){
                // location Already granted update the location
            }else{
                // Request location permission
             requestPermissionLauncher.launch(
                 arrayOf(
                     Manifest.permission.ACCESS_FINE_LOCATION,
                     Manifest.permission.ACCESS_COARSE_LOCATION
                 )
             )
            }
        }) {

        }
    }
}