package com.example.monarch_android

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class DialogController(context: Context, act: Activity) {
    // Handles dialogs (alerts, popups, etc)
    private val activityContext: Context = context
    private val activity: Activity = act

    fun alert(title: Int, msg: Int) {
        // Shows generic alert with a single button ("OK")
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { _, _ -> }  // no callback
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun multChoice(title: Int, choices: Array<String>, choiceCallback: (choice: Int) -> Unit) {
        // User chooses from a list of items with an additional "cancel" button
        var selection = -1
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(title)
        builder.setSingleChoiceItems(choices, -1) { dialog, which ->
            selection = which
        }
        builder.setPositiveButton("OK") { dialog, which ->
            choiceCallback(selection)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            choiceCallback(-1)
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}