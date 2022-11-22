package com.my.newvoicetyping.keyboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.my.newvoicetyping.R

class PreferenceActivity_voicetyping : android.preference.PreferenceActivity() {
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //   getActionBar().setTitle("Settings");
        try {
            fragmentManager
                .beginTransaction()
                .replace(android.R.id.content,
                    UrduSimplePreferenceFragment()).commit()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (ingored: Exception) {
        }
    }

    @SuppressLint("ValidFragment")
    class UrduSimplePreferenceFragment : PreferenceFragment() {
        @SuppressLint("NewApi")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
      //  AdsHandling.getInstance().showAppLovinInterstitial(this@PreferenceActivity_sindhiBest)
    }
}