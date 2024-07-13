package com.example.plantification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Composable function to request multiple permissions in a Compose-based Android application.
 *
 * @param permissions List of permission strings to request.
 */
@ExperimentalPermissionsApi
@Composable
fun RequestMultiplePermissions(
    permissions: List<String>,
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    HandleRequests(
        multiplePermissionsState = multiplePermissionsState,
    )
}

/**
 * Composable function to handle permission requests.
 *
 * @param multiplePermissionsState The state representing multiple permissions.
 */
@ExperimentalPermissionsApi
@Composable
private fun HandleRequests(
    multiplePermissionsState: MultiplePermissionsState,
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val result = multiplePermissionsState.permissions.all {
        shouldShowRationale = it.status.shouldShowRationale
        it.status == PermissionStatus.Granted
    }
    if (!result) {
        PermissionDeniedContent(
            shouldShowRationale = shouldShowRationale,
            multiplePermissionsState = multiplePermissionsState
        )
    }
}

/**
 * Composable function to display content when permissions are denied.
 *
 * @param shouldShowRationale Flag indicating whether rationale should be shown.
 * @param multiplePermissionsState The state representing multiple permissions.
 */
@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    shouldShowRationale: Boolean,
    multiplePermissionsState: MultiplePermissionsState,
) {
    if (shouldShowRationale) {
        // Display an AlertDialog explaining the need for permissions
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                )
            },
            text = {
                Text("To use this app you need to give us the permissions.")
            },
            confirmButton = {
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                    Text("Give Permission")
                }
            }
        )
    } else {
        // Display a message and a button to request permissions
        Box(modifier = Modifier.background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Give this app a permission to proceed. If it doesn't work, then you'll have change it from the settings from the settings",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                    Text(text = "Request")
                }
            }
        }
    }
}
