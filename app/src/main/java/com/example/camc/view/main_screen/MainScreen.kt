package com.example.camc.view.main_screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Button
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.camc.view.permission_screen.CoarseLocationPermissionTextProvider
import com.example.camc.view.permission_screen.FineLocationPermissionTextProvider
import com.example.camc.view.permission_screen.PermissionDialog



@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val permissionsToRequest = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        multiplePermissionResultLauncher.launch(permissionsToRequest)
    }



    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        CoarseLocationPermissionTextProvider()
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        FineLocationPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    LocalContext.current as Activity,
                    permission
                ),
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = openAppSettings()
            )
        }


    var text by remember {
        mutableStateOf("")
    }
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        MainScreenContent()

    }

}
@Composable
fun openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", LocalContext.current.packageName, null)
    )
    LocalContext.current.startActivity(intent)
}

@Composable
@Preview(showBackground = true)
fun MainScreenContent(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current


    Column(modifier = modifier.fillMaxSize()
                              .padding(vertical= 70.dp, horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(65.dp))
        Text("Willkommen zur App für das zweite Praktikum zur Datenauswertung, maschinellen Lernen und Erkennung von Aktivitätsmustern")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            uriHandler.openUri("https://github.com/AlexKirovhsbo/CAMC")
        }) {
            Text("Github")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Sensoren können über das erweiterte Menü ausgewählt werden. Ebenfalls kann man zwischen der Datenaufzeichnung für menschliche Bewegung und für die Carrerabahn wählen.")
        Spacer(modifier = Modifier.height(5.dp))
        Text("Die gesammelten Daten lassen sich als csv exportieren für die einzelnen genutzten Sensoren und gesammelt.")
        Spacer(modifier = Modifier.height(5.dp))
        Text("Jeder Sensor kann bei Bedarf seine Werte in die Room-Datenbank schreiben.")
        Spacer(modifier = Modifier.height(5.dp))
        Text("Der Prozess des Schreibens und die Werkzeuge zum Anschauen der Messreihe können mit dem \"Start Recording\" Knopf gestartet werden.")
        Spacer(modifier = Modifier.height(5.dp))
        Text("Anpassung je Seite erfolgt über die Einstellungen.")
    }
}