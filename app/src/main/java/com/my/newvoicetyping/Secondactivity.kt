package com.my.newvoicetyping

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import com.my.newvoicetyping.Utils.checkIfKeyboardEnabled
import com.my.newvoicetyping.Utils.isInputMethodEnabled
import com.my.newvoicetyping.databinding.ActivitySecondactivityBinding

class Secondactivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondactivityBinding
    private var setBtnClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        nextMove()
        if (checkIfKeyboardEnabled()) {
            binding.btnactivatekeyboard.setImageDrawable(getDrawable(R.drawable.dullbtnback))
            binding.btnactivatekeyboard.isClickable = false
        }
        binding.btnactivatekeyboard.setOnClickListener {
            try {
                startActivityForResult(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 0)
                MySharedPreferences.setIsFromPickIntent(this, true)
                // startActivity(Intent(this,exploreactivity::class.java))
            } catch (ignored: ActivityNotFoundException) {
            }
        }
        binding.btnswitchkeyboard.setOnClickListener {

            try {
                val mgr = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.showInputMethodPicker()
                setBtnClicked = true
                MySharedPreferences.setIsFromPickIntent(this, true)

            } catch (ignored: ActivityNotFoundException) {
            }
        }

    }

    override fun onRestart() {
        super.onRestart()
        if (checkIfKeyboardEnabled()) {
            binding.btnactivatekeyboard.setImageDrawable(getDrawable(R.drawable.dullactivatekeyboardbtn))
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.enabledInputMethodList.toString()
                    .contains(packageName) && setBtnClicked
            ) {
                setBtnClicked = false
                if (checkIfKeyboardEnabled()) {
                    if (isInputMethodEnabled()) {
                        startActivity(Intent(this@Secondactivity, Appmainactivity::class.java))
                    }

                }
            }
        }


    }
    private fun nextMove() {
        if (checkIfKeyboardEnabled()) {
            if (isInputMethodEnabled()) {
                startActivity(Intent(this@Secondactivity, Appmainactivity::class.java))
            }
        }

    }
}
