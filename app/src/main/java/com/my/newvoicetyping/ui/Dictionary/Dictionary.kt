package com.my.newvoicetyping.ui.Dictionary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.my.newvoicetyping.R
import com.my.newvoicetyping.Roomdb.models.dictionaryhistory
import com.my.newvoicetyping.Roomdb.viewmodels.historyviewmodel
import com.my.newvoicetyping.Utils.MySingleton
import com.my.newvoicetyping.Utils.networkutils
import com.my.newvoicetyping.databinding.FragmentDictionaryBinding
import com.my.newvoicetyping.databinding.FragmentSpeechtotextBinding
import com.my.newvoicetyping.ui.Speechtotext.SpeechtotextArgs
import com.my.voicetyping.dictionaryviewmodelfactory.dictviewmodelfactory
import org.json.JSONArray
import java.util.*

class Dictionary : Fragment(),TextToSpeech.OnInitListener {

    private var _binding: FragmentDictionaryBinding? = null
    lateinit var navController: NavController
    val args: SpeechtotextArgs by navArgs()
    private var tts: TextToSpeech? = null
    private val binding get() = _binding!!
    lateinit var myviewmodel: historyviewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.pbloadingword.visibility = View.GONE
        myviewmodel= androidx.lifecycle.ViewModelProvider(
            this,
            dictviewmodelfactory(requireActivity().application)
        )[historyviewmodel::class.java]

        tts = TextToSpeech(requireContext(), this)
        binding.btnsearchword.setOnClickListener {
            Log.d("Btn clicked", "btn clicked")
            if (networkutils.isNetworkAvailable(requireContext())) {

                val word: String = binding.etword.text.toString()
                getworddetails(word)

                hideKeyboard(requireActivity())
            } else {
                Toast.makeText(requireContext(), "No Internet Connection", Toast.LENGTH_LONG).show()
            }
        }

        binding.cardview.visibility = View.GONE

        binding.btncancel.setOnClickListener {
            binding.etword.text.clear()
            val word: String = binding.etword.text.toString()
            if (word == "") {

                binding.cardview.visibility = View.GONE
            }
        }
        binding.btncopttocb.setOnClickListener {
            val text = binding.etword.text.toString()
            copytexttoclipboard(text)
        }
        binding.btnspeaker.setOnClickListener {
            speakOut()
        }



        return root

    }
    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun getworddetails(word: String) {
        binding.pbloadingword.visibility = View.VISIBLE
        var mStatusCode = 0;
        //val queue = Volley.newRequestQueue(this)
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"
        val request: JsonArrayRequest = @SuppressLint("ClickableViewAccessibility")
        object : JsonArrayRequest(url,
            Response.Listener { response ->
                val responseObj = response.getJSONObject(0)

                binding.cardview.visibility = View.VISIBLE
                //word
                val word = responseObj.getString("word")
                val meanings = responseObj.getJSONArray("meanings")
                val meaninindex0 = meanings.getJSONObject(0)
                val definitions = meaninindex0.getJSONArray("definitions")
                val actualdef = definitions.getJSONObject(0)
                binding.pbloadingword.visibility = View.GONE
                binding.linear1.visibility = View.VISIBLE
                binding.tvWord.text = "$word"

                try {
                    binding.linear2.visibility = View.GONE
                    val showingdefinition = actualdef.getString("definition")
                    Log.d("Definition", showingdefinition)
                    binding.linear2.visibility = View.VISIBLE
                    binding.tvSentence1.text = "1:$showingdefinition"

                    val dictionaryhistory=dictionaryhistory(word = word, definition = showingdefinition)
                    myviewmodel.addwords(dictionaryhistory)

                    binding.tvSample.setOnTouchListener(View.OnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            speakOutSampleDef()
                            return@OnTouchListener true
                        }
                        false
                    })

                    val meaninindex1 = meanings.getJSONObject(1)
                    val definitions1 = meaninindex1.getJSONArray("definitions")
                    val actualdef1 = definitions1.getJSONObject(0)
                    val showingdefinition1 = actualdef1.getString("definition")
                    binding.tvSentence2.text = "2:$showingdefinition1"
                    //def2
                    Log.d("Definition1", showingdefinition1)

                    val webmeaninindex1 = meanings.getJSONObject(2)
                    val webdefinitions1 = webmeaninindex1.getJSONArray("definitions")
                    val webactualdef1 = webdefinitions1.getJSONObject(0)
                    val webshowingdefinition1 = webactualdef1.getString("definition")
                    Log.d("web Definition1", webshowingdefinition1)
                    binding.linear3.visibility = View.VISIBLE
                    binding.tvWebDef1.text = "1:$webshowingdefinition1"
                    //webdef2
                    val webmeaninindex2 = meanings.getJSONObject(2)
                    val webdefinitions2 = webmeaninindex2.getJSONArray("definitions")
                    val webactualdef2 = webdefinitions2.getJSONObject(1)
                    val webshowingdefinition2 = webactualdef2.getString("definition")
                    Log.d("web Definition2", webshowingdefinition2)
                    binding.tvWebDef2.text = "2:$webshowingdefinition2"
                    binding.tvWebDef.setOnTouchListener(View.OnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            speakOutWevDef()
                            return@OnTouchListener true
                        }
                        false
                    })


                } catch (e: Exception) {
                    binding.linear3.visibility = View.GONE
                    Log.d("Error", e.message.toString())
                }
                Log.i("statusCode", java.lang.String.valueOf(mStatusCode))
            },
            Response.ErrorListener { error ->
                binding.cardview.visibility = View.GONE
                binding.pbloadingword.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Please Enter Correct Word",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray>? {
                if (response != null) {
                    mStatusCode = response.statusCode
                }
                return super.parseNetworkResponse(response)
            }
        }

        MySingleton.getInstance(requireContext()).addToRequestQueue(request)
    }

    fun copytexttoclipboard(text: String) {
        val clipboard: ClipboardManager =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Text Copied", Toast.LENGTH_SHORT).show()
    }

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

    private fun speakOut() {
        val text = binding.etword.text.trim().toString()
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    private fun speakOutSampleDef() {
        val text =
            binding.tvSentence1.text.trim().toString() + binding.tvSentence2.text.trim().toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun speakOutWevDef() {
        val text =
            binding.tvWebDef1.text.trim().toString() + binding.tvWebDef2.text.trim().toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        if (tts != null) {
            tts!!.stop()
            //    tts!!.shutdown()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}