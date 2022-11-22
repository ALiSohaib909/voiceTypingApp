package com.my.newvoicetyping.ui.Speechtotext

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.my.newvoicetyping.R
import com.my.newvoicetyping.databinding.FragmentSpeechtotextBinding

import java.util.*


class Speechtotext : Fragment(),TextToSpeech.OnInitListener {

    private var _binding: FragmentSpeechtotextBinding? = null
    lateinit var navController: NavController
    val args:SpeechtotextArgs by navArgs()
    lateinit var fromlanguacode: String
    lateinit var tolanguacode: String
   lateinit var fromlanguaname: String
    private var tts: TextToSpeech? = null
    var fromcountryflag: Int = R.drawable.united_states
    lateinit var tolanguaname: String
    var tocountryflag: Int = R.drawable.spain
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpeechtotextBinding.inflate(inflater, container, false)
        val root: View = binding.root
        tts = TextToSpeech(requireContext(), this)
        binding.pbSpeechToText.visibility=View.GONE
        navController=findNavController()
        fromlanguaname=args.fromcountryname
        val fromcountryflag=args.fromcountryflag
        tolanguaname=args.tocountryname
        val tocountryflag=args.tocountryflag
        binding.tvtranslatedtext.visibility=View.GONE
        binding.btnspeaker1.visibility=View.GONE
        binding.btncopytext1.visibility=View.GONE
        binding.btnshare.visibility=View.GONE
        binding.ivfromflag.background = activity?.resources!!.getDrawable(fromcountryflag)
        binding.tocountryflag.background = activity?.resources!!.getDrawable(tocountryflag)
        binding.tvfromcountryname.text=fromlanguaname
        binding.tvtocountryname.text=tolanguaname
        fromlanguacode=getfromlanguagecode(fromlanguaname)
        tolanguacode=getfromlanguagecode(tolanguaname)


        binding.btnfrom.setOnClickListener {
            val action=SpeechtotextDirections.actionSpeechtotextToFromcountryFragment(tolanguaname,tocountryflag)
            navController.navigate(action)
        }
        binding.btnto.setOnClickListener {
            val action=SpeechtotextDirections.actionSpeechtotextToTocountryFragment(fromlanguaname,fromcountryflag)
            navController.navigate(action)
        }
        // mic button click
        binding.btnmic.setOnClickListener {
            askforpermission()
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    getfromlanguagecode(fromlanguaname)
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak To convert into text")
                try {
                    startActivityForResult(intent, 101)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        }
        // translate button click
        binding.btntranslate.setOnClickListener {
            val sourcetext = binding.etsource.text.toString()
            if (sourcetext.isEmpty()) {
                Toast.makeText(requireContext(), "Source Text is Empty", Toast.LENGTH_SHORT).show()
            } else if (fromlanguacode == "null") {
                Toast.makeText(requireContext(), "Please Select A Source Language", Toast.LENGTH_SHORT).show()

            } else if (tolanguacode == "null") {
                Toast.makeText(requireContext(), "Please Select A Language for translation", Toast.LENGTH_SHORT)
                    .show()

            } else {
                translatetext(fromlanguacode, tolanguacode, sourcetext)
            }
        }
        // speaker button click
        binding.btnspeaker.setOnClickListener {
            val text=binding.etsource.text.toString()
            speakOut(text)
        }
        // 2nd  speaker button click
        binding.btnspeaker1.setOnClickListener {
            val text=binding.tvtranslatedtext.text.toString()
            speakOut(text)
        }
        // 1st copy button click
        binding.btncopytext.setOnClickListener {
         val text=binding.etsource.text.toString()
            copytexttoclipborad(text)
        }
        // 2nd copy button click
        binding.btncopytext1.setOnClickListener {
            val text=binding.tvtranslatedtext.text.toString()
            copytexttoclipborad(text)
        }

        return root
    }

    // Text to speech code
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            for (locale in Locale.getAvailableLocales()) {
                Log.d(
                    "LOCALES",
                    locale.language + "_" + locale.country + " [" + locale.displayName + "]"
                )
            }
            val result = tts!!.setLanguage(Locale.getDefault())

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            }
        }
    }

    private fun speakOut(text:String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    // copy text to clipboard
    fun copytexttoclipborad(text: String) {
        //getActivity()?.getSystemService(name)
        val clipboard: ClipboardManager =
            activity?. getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show()
    }

    // asking for permissions
    fun askforpermission() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }
    // Getting spoken text
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val result: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                binding.etsource.setText(
                    Objects.requireNonNull(result)[0]
                )
            }
        }
    }
    // Translating text
    fun translatetext(fomlanguagecode: String, tolanguagecode: String, source: String) {
        binding.pbSpeechToText.visibility = View.VISIBLE
        Log.d("Language", fromlanguacode)
        val options =
            TranslatorOptions.Builder().setSourceLanguage(fomlanguagecode).setTargetLanguage(
                tolanguagecode
            ).build()
        val firebasetranslater = Translation.getClient(options)
        val firebaseModelDownloadConditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        firebasetranslater.downloadModelIfNeeded(firebaseModelDownloadConditions)
            .addOnSuccessListener {
                firebasetranslater.translate(source).addOnSuccessListener {


                    binding.btnspeaker1.visibility=View.VISIBLE
                    binding.btncopytext1.visibility=View.VISIBLE
                    binding.btnshare.visibility=View.VISIBLE
                    binding.tvtranslatedtext.visibility = View.VISIBLE
                    binding.tvtranslatedtext.setText(it.toString())
                    val text = it.toString()
                    Log.d("Translated", text)
                    binding.pbSpeechToText.visibility = View.GONE
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }

    }
    // Getting Language Code
    fun getfromlanguagecode(language: String): String {
        var langaugecode: String
        when (language) {
            "English" -> {
                langaugecode = TranslateLanguage.ENGLISH
                return langaugecode
            }
            "Afrikaans" -> {
                langaugecode = TranslateLanguage.AFRIKAANS
                return langaugecode
            }
            "Arabic" -> {
                langaugecode = TranslateLanguage.ARABIC
                return langaugecode
            }
            "Belarusian" -> {
                langaugecode = TranslateLanguage.BELARUSIAN
                return langaugecode
            }
            "Bengali" -> {
                langaugecode = TranslateLanguage.BENGALI
                return langaugecode
            }
            "Catalan" -> {
                langaugecode = TranslateLanguage.CATALAN
                return langaugecode
            }
            "Czech" -> {
                langaugecode = TranslateLanguage.CZECH
                return langaugecode
            }
            "Welsh" -> {
                langaugecode = TranslateLanguage.WELSH
                return langaugecode
            }
            "Hindi" -> {
                langaugecode = TranslateLanguage.HINDI
                return langaugecode
            }
            "Urdu" -> {
                langaugecode = TranslateLanguage.URDU
                return langaugecode
            }
            "Spanish" -> {
                langaugecode = TranslateLanguage.SPANISH
                return langaugecode
            }
            "German" -> {
                langaugecode = TranslateLanguage.GERMAN
                return langaugecode
            }
            "Greek" -> {
                langaugecode = TranslateLanguage.GREEK
                return langaugecode
            }
            "Esperanto" -> {
                langaugecode = TranslateLanguage.ESPERANTO
                return langaugecode
            }
            "Estonian" -> {
                langaugecode = TranslateLanguage.ESTONIAN
                return langaugecode
            }
            "Persian" -> {
                langaugecode = TranslateLanguage.PERSIAN
                return langaugecode
            }
            "Finnish" -> {
                langaugecode = TranslateLanguage.FINNISH
                return langaugecode
            }
            "French" -> {
                langaugecode = TranslateLanguage.FRENCH
                return langaugecode
            }
            "Irish" -> {
                langaugecode = TranslateLanguage.IRISH
                return langaugecode
            }
            "Galician" -> {
                langaugecode = TranslateLanguage.GALICIAN
                return langaugecode
            }
            "Gujarati" -> {
                langaugecode = TranslateLanguage.GUJARATI
                return langaugecode
            }
            "Hebrew" -> {
                langaugecode = TranslateLanguage.HEBREW
                return langaugecode
            }
            "Croatian" -> {
                langaugecode = TranslateLanguage.CROATIAN
                return langaugecode
            }
            "Haitian" -> {
                langaugecode = TranslateLanguage.HAITIAN_CREOLE
                return langaugecode
            }
            "Hungarian" -> {
                langaugecode = TranslateLanguage.HUNGARIAN
                return langaugecode
            }
            "Indonesian" -> {
                langaugecode = TranslateLanguage.INDONESIAN
                return langaugecode
            }
            "Icelandic" -> {
                langaugecode = TranslateLanguage.ICELANDIC
                return langaugecode
            }
            "Italian" -> {
                langaugecode = TranslateLanguage.ITALIAN
                return langaugecode
            }
            "Japanese" -> {
                langaugecode = TranslateLanguage.JAPANESE
                return langaugecode
            }
            "Georgian" -> {
                langaugecode = TranslateLanguage.GEORGIAN
                return langaugecode
            }
            "Kannada" -> {
                langaugecode = TranslateLanguage.KANNADA
                return langaugecode
            }
            "Korean" -> {
                langaugecode = TranslateLanguage.KOREAN
                return langaugecode
            }
            "Lithuanian" -> {
                langaugecode = TranslateLanguage.LITHUANIAN
                return langaugecode
            }
            "Latvian" -> {
                langaugecode = TranslateLanguage.LATVIAN
                return langaugecode
            }
            "Macedonian" -> {
                langaugecode = TranslateLanguage.MACEDONIAN
                return langaugecode
            }
            "Marathi" -> {
                langaugecode = TranslateLanguage.MARATHI
                return langaugecode
            }
            "Malay" -> {
                langaugecode = TranslateLanguage.MALAY
                return langaugecode
            }
            "Maltese" -> {
                langaugecode = TranslateLanguage.MALTESE
                return langaugecode
            }
            "Dutch" -> {
                langaugecode = TranslateLanguage.DUTCH
                return langaugecode
            }
            "Norwegian" -> {
                langaugecode = TranslateLanguage.NORWEGIAN
                return langaugecode
            }
            "Polish" -> {
                langaugecode = TranslateLanguage.POLISH
                return langaugecode
            }
            "Portuguese" -> {
                langaugecode = TranslateLanguage.PORTUGUESE
                return langaugecode
            }
            "Romanian" -> {
                langaugecode = TranslateLanguage.ROMANIAN
                return langaugecode
            }
            "Russian" -> {
                langaugecode = TranslateLanguage.RUSSIAN
                return langaugecode
            }
            "Slovak" -> {
                langaugecode = TranslateLanguage.SLOVAK
                return langaugecode
            }
            "Albanian" -> {
                langaugecode = TranslateLanguage.ALBANIAN
                return langaugecode
            }
            "Swedish" -> {
                langaugecode = TranslateLanguage.SWEDISH
                return langaugecode
            }
            "Swahili" -> {
                langaugecode = TranslateLanguage.SWAHILI
                return langaugecode
            }
            "Tamil" -> {
                langaugecode = TranslateLanguage.TAMIL
                return langaugecode
            }
            "Telugu" -> {
                langaugecode = TranslateLanguage.TELUGU
                return langaugecode
            }
            "Thai" -> {
                langaugecode = TranslateLanguage.THAI
                return langaugecode
            }
            "Tagalog" -> {
                langaugecode = TranslateLanguage.TAGALOG
                return langaugecode
            }
            "Turkish" -> {
                langaugecode = TranslateLanguage.TURKISH
                return langaugecode
            }
            "Ukrainian" -> {
                langaugecode = TranslateLanguage.UKRAINIAN
                return langaugecode
            }
            "Vietnamese" -> {
                langaugecode = TranslateLanguage.VIETNAMESE
                return langaugecode
            }
            "Chinese" -> {
                langaugecode = TranslateLanguage.CHINESE
                return langaugecode
            }
            else -> {
                langaugecode = ""
                return langaugecode
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}