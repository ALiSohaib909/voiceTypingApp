package com.my.newvoicetyping.Utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.my.newvoicetyping.keyboard.SoftKeyboard_sindhiBest


fun Activity.isInputMethodEnabled(): Boolean {
    return ComponentName(
        this,
        SoftKeyboard_sindhiBest::class.java
    ) == ComponentName.unflattenFromString(
        Settings.Secure.getString(contentResolver, "default_input_method")
    )
}

fun Activity.checkIfKeyboardEnabled(): Boolean {
    val packageLocal = packageName
    val isInputDeviceEnabled = false
    val inputMethodManager =
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    val list =
        inputMethodManager.enabledInputMethodList

    // check if our keyboard is enabled as input method
    for (inputMethod in list) {
        val packageName = inputMethod.packageName
        if (packageName == packageLocal) {
            return true
        }
    }
    return false
}
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}