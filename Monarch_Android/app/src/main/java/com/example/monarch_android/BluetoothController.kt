package com.example.monarch_android

import android.content.Context
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.app.AlertDialog
import android.widget.Toast

class BluetoothController (private val context: Context) {
    // Handles bluetooth setup and communicating with the boi (butterfly)
    private val activityContext: Context = context;
    private val bluetoothManager: BluetoothManager = activityContext.getSystemService(BluetoothManager::class.java);
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter;
    private val bluetoothSupport: Boolean = (bluetoothAdapter !== null);
    private var isConnected: Boolean = false;

    init {
        if (!bluetoothSupport) {
            // Device doesn't support Bluetooth
            alert(R.string.no_bluetooth_title, R.string.no_bluetooth_msg);
        }
    }

    fun isConnected(): Boolean {
        // Returns true if phone is connected to butterfly
        return isConnected;
    }

    fun getPairedDevices(): Set<BluetoothDevice>? {
        return null;
    }

    fun alert(title: Int, msg: Int) {
        // Shows generic alert with a single button ("OK")
        val builder = AlertDialog.Builder(activityContext);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK") { _, _ -> };  // no callback
        val alertDialog: AlertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}