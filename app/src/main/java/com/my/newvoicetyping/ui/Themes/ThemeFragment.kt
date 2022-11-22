package com.my.newvoicetyping.ui.Themes

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.newvoicetyping.R
import com.my.newvoicetyping.databinding.FragmentThemeBinding
import com.my.newvoicetyping.ui.Speechtotext.SpeechtotextArgs

class ThemeFragment : Fragment(), themeCheckListener {
    private var _binding: FragmentThemeBinding? = null
    lateinit var navController: NavController
    val args: SpeechtotextArgs by navArgs()
    private var tts: TextToSpeech? = null
    private val binding get() = _binding!!

    lateinit var themeAdapter: ThemesAdapter
    var arrayList = ArrayList<Int>()
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var mDrawableTheme: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(requireContext())
        mSharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(requireContext())

        arrayList.add((R.drawable.board_theme_white))
        arrayList.add((R.drawable.board_theme__black))
        arrayList.add((R.drawable.board_theme_aqua))
        arrayList.add((R.drawable.board_theme__red))
        arrayList.add((R.drawable.board_theme_green))
        arrayList.add((R.drawable.board_theme_meroon))
        arrayList.add((R.drawable.board_theme_pink))
        arrayList.add((R.drawable.board_theme_dark_green))
        arrayList.add((R.drawable.board_theme_purple))
        arrayList.add((R.drawable.board_theme_yellow))
        arrayList.add((R.drawable.board_theme_blue))
        arrayList.add((R.drawable.board_theme_12))
        arrayList.add((R.drawable.board_theme_13))
        arrayList.add((R.drawable.board_theme_14))
        arrayList.add((R.drawable.board_theme_15))

        themeAdapter = ThemesAdapter(this)
        themeAdapter.getthemesimages(arrayList)
        binding.rvThemes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvThemes.adapter = themeAdapter
    }

    override fun onThemeChecked(themesModel: Int) {
        editor = mSharedPreferences.edit()
        editor.putInt("myTheme", themesModel)
        Log.d("KeyboardApplied2",themesModel.toString())
        editor.commit()
    }
}