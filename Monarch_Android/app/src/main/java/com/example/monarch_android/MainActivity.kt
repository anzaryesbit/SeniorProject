package com.example.monarch_android

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.monarch_android.ui.theme.Monarch_AndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val activityContext: Context = this
        val activity: Activity = this@MainActivity

        val dialogController: DialogController = DialogController(activityContext, activity)
        val bluetoothController: BluetoothController = BluetoothController(activityContext, activity)

        fun chooseDeviceAndConnect(devices: ArrayList<BluetoothDevice>) {
            // Asks the user to pick from a list of open bluetooth devices and connects to the user's choice
            if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf<String>(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )
            }
            if (devices.isEmpty()) {
                // No available devices to connect to; alert the user
                dialogController.alert(R.string.device_not_found_title, R.string.device_not_found_msg)
            } else {
                // Create a multiple choice list and let the user choose device to connect to
                val deviceNameList: List<String> = devices.map { device: BluetoothDevice -> device.name }
                val deviceNameArray: Array<String> = deviceNameList.toTypedArray()
                dialogController.multChoice(
                    R.string.choose_device_title,
                    deviceNameArray
                ) { choice: Int ->
                    if (choice == -1) {
                        Toast.makeText(activityContext, "No device connected", Toast.LENGTH_SHORT).show()
                    } else {
                        // TODO: connect to address specified by devices[choice].address
                        // This is where the connection code is written
                        println("TEST: User chose device " + devices[choice].name)
                    }
                }
            }
        }

        // Initialize layouts and stuff
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for bluetooth compatibility
        if (!bluetoothController.bluetoothSupport) {
            dialogController.alert(R.string.no_bluetooth_title, R.string.no_bluetooth_msg)
        }

        // Test bluetooth scan
        println("TEST: Testing ble scan")
        bluetoothController.bluetoothScan(::chooseDeviceAndConnect)

        // Add event handlers for buttons
        // TODO: when bluetooth button is clicked, trigger bluetooth scan w/ chooseDeviceAndConnect as callback
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Bruh $name",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Monarch_AndroidTheme {
        Greeting("Android")
    }
}