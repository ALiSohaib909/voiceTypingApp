package com.my.newvoicetyping.ui.home



import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.my.newvoicetyping.MySharedPreferences
import com.my.newvoicetyping.R
import com.my.newvoicetyping.databinding.FragmentHomeBinding
import com.my.newvoicetyping.keyboard.PreferenceActivity_voicetyping


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var navController: NavController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        navController = findNavController()


        binding.ivSpeechToText.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToSpeechtotext(
                "English",
                R.drawable.welsh,
                "Spanish",
                R.drawable.spainish
            )
            navController.navigate(action)
        }
        binding.ivDictionary.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToDictionary()
            navController.navigate(action)
        }
        binding.ivSettings.setOnClickListener {
            val intent = Intent(
                requireContext(),
                PreferenceActivity_voicetyping::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.ivThemes.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToThemeFragment2()
            navController.navigate(action)
        }
        binding.ivDisable.setOnClickListener {
            try {
                startActivityForResult(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS), 0)
                MySharedPreferences.setIsFromPickIntent(requireContext(), true)
                // startActivity(Intent(this,exploreactivity::class.java))
            } catch (ignored: ActivityNotFoundException) {
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}