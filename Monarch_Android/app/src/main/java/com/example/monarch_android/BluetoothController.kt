package com.example.monarch_android

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.app.ActivityCompat

class BluetoothController (context: Context, act: Activity) {
    // Handles bluetooth setup and communicating with the boi (butterfly)
    private val activityContext: Context = context
    private val activity: Activity = act
    private val bluetoothManager: BluetoothManager = activityContext.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val bluetoothScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private val handler: Handler = Handler(Looper.getMainLooper())      // lets us execute stuff after delay
    private var isScanning: Boolean = false
    private var isConnected: Boolean = false

    val bleDeviceList: BLEDeviceList = BLEDeviceList()
    val bluetoothSupport: Boolean = (bluetoothAdapter !== null)         // TODO: run this check before every function
                                                                        // TODO: also add check for BLE specifically

    fun bluetoothScan(completeScanCallback: (devices: ArrayList<BluetoothDevice>) -> Unit) {
        // Scans for BLE devices
        if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf<String>(Manifest.permission.BLUETOOTH_SCAN),
                1
            )
        }
        if (!isScanning) {
            // If not already scanning, start scanning, then stop after 10 seconds
            handler.postDelayed({
                isScanning = false
                bluetoothScanner?.stopScan(bluetoothScanCallback)
                completeScanCallback(bleDeviceList.getDevices())
            }, 10000)
            isScanning = true
            bleDeviceList.clear()
            bluetoothScanner?.startScan(bluetoothScanCallback)
        } else {
            // If already scanning, stop scanning
            isScanning = false
            bluetoothScanner?.stopScan(bluetoothScanCallback)
            completeScanCallback(bleDeviceList.getDevices())
        }
    }

    private val bluetoothScanCallback: ScanCallback = object: ScanCallback() {
        // Callback for when devices are found during BLE scan
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf<String>(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )
            }
            super.onScanResult(callbackType, result)
            if (result.device.name != null)
                bleDeviceList.addDevice(result.device)
        }
    }

    inner class BLEDeviceList {
        // List to hold bluetooth devices when scanning
        private val mLeDevices: ArrayList<BluetoothDevice> = ArrayList<BluetoothDevice>()

        fun getDevices(): ArrayList<BluetoothDevice> {
            return mLeDevices
        }

        fun addDevice(device: BluetoothDevice) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device)
            }
        }

        fun clear() {
            mLeDevices.clear()
        }

        fun getCount(): Int {
            return mLeDevices.size
        }
    }

    class ConnectThread(address: String): Thread() {
        // Connects to bluetooth device
    }

    class ReadWriteThread(address: String): Thread() {
        // Read from and write to bluetooth device
    }

    fun isConnected(): Boolean {
        // Returns true if phone is connected to butterfly
        return isConnected
    }
}