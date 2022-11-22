package com.my.newvoicetyping.ui.Speechtotext

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.newvoicetyping.R
import com.my.newvoicetyping.databinding.FragmentFromcountryFragmentBinding
import com.my.newvoicetyping.databinding.FragmentTocountryFragmentBinding

class tocountry_fragment : Fragment(),countryitemclicklistner {

    private var _binding: FragmentTocountryFragmentBinding? = null
    lateinit var navController: NavController
    private val binding get() = _binding!!
    lateinit var adapter:countryrvadapter
    val args:tocountry_fragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTocountryFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        adapter= countryrvadapter(this)
        navController=findNavController()
        val countries = ArrayList<countrylife>()
        countries.add(countrylife("English", R.drawable.welsh))
        countries.add(countrylife("Afrikaans", R.drawable.africa))
        countries.add(countrylife("Arabic", R.drawable.arabic))
        countries.add(countrylife("Belarusian", R.drawable.belarussian))
        countries.add(countrylife("Bulgarian", R.drawable.balghariaa))
        countries.add(countrylife("Bengali", R.drawable.bangla))
        countries.add(countrylife("Catalan", R.drawable.spainish))
        countries.add(countrylife("Czech", R.drawable.chechz))
        countries.add(countrylife("Welsh", R.drawable.welsh))
        countries.add(countrylife("Hindi", R.drawable.india))
        countries.add(countrylife("Urdu", R.drawable.pakistan))
        countries.add(countrylife("Spanish", R.drawable.spain))
        countries.add(countrylife("German", R.drawable.german))
        countries.add(countrylife("Greek", R.drawable.greek))
        countries.add(countrylife("Esperanto", R.drawable.esparanto))
        countries.add(countrylife("Estonian", R.drawable.estonina))
        countries.add(countrylife("Persian", R.drawable.persian))
        countries.add(countrylife("Finnish", R.drawable.finnish))
        countries.add(countrylife("French", R.drawable.france))
        countries.add(countrylife("Irish", R.drawable.irish))
        countries.add(countrylife("Galician", R.drawable.galician))
        countries.add(countrylife("Hebrew", R.drawable.hebrew))
        countries.add(countrylife("Croatian", R.drawable.croation))
        countries.add(countrylife("Haitian", R.drawable.haitian))
        countries.add(countrylife("Hungarian", R.drawable.hungary))
        countries.add(countrylife("Indonesian", R.drawable.indonashia))
        countries.add(countrylife("Icelandic", R.drawable.iceland))
        countries.add(countrylife("Italian", R.drawable.italy))
        countries.add(countrylife("Japanese", R.drawable.japnes))
        countries.add(countrylife("Georgian", R.drawable.georgia))
        countries.add(countrylife("Kannada", R.drawable.kannada))
        countries.add(countrylife("Korean", R.drawable.korea))
        countries.add(countrylife("Lithuanian", R.drawable.lathuhnia))
        countries.add(countrylife("Latvian", R.drawable.latvia))
        countries.add(countrylife("Macedonian", R.drawable.macedonia))
        countries.add(countrylife("Malay", R.drawable.malay))
        countries.add(countrylife("Maltese", R.drawable.malta))
        countries.add(countrylife("Dutch", R.drawable.dutch))
        countries.add(countrylife("Norwegian", R.drawable.norway))
        countries.add(countrylife("Polish", R.drawable.poland))
        countries.add(countrylife("Portuguese", R.drawable.portgual))
        countries.add(countrylife("Romanian", R.drawable.romania))
        countries.add(countrylife("Russian", R.drawable.russian))
        countries.add(countrylife("Slovak", R.drawable.slovakia))
        countries.add(countrylife("Albanian", R.drawable.albania))
        countries.add(countrylife("Swedish", R.drawable.swedan))
        countries.add(countrylife("Gujarati", R.drawable.india))
        countries.add(countrylife("Marathi", R.drawable.india))
        countries.add(countrylife("Swahili", R.drawable.kenya))
        countries.add(countrylife("Tamil", R.drawable.india))
        countries.add(countrylife("Telugu", R.drawable.india))
        countries.add(countrylife("Tagalog", R.drawable.phillipines))
        countries.add(countrylife("Thai", R.drawable.thai))
        countries.add(countrylife("Turkish", R.drawable.turkish))
        countries.add(countrylife("Ukrainian", R.drawable.ukrainian))
        countries.add(countrylife("Vietnamese", R.drawable.vaitname))
        countries.add(countrylife("Chinese", R.drawable.china))
        adapter.getcountryinfo(countries)

        binding.rvtocountry.layoutManager= LinearLayoutManager(requireContext())
        binding.rvtocountry.adapter=adapter

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun oncountryitemclicked(countrylife: countrylife) {
        val fromcountryname=args.fromcountryname
        val fromcountryflag=args.fromcountryflag
       // navController.popBackStack(R.id.speechtotext, true);
        val action =tocountry_fragmentDirections.actionTocountryFragmentToSpeechtotext(fromcountryname,fromcountryflag,countrylife.countryname,countrylife.flagicon)
       // val action =fromcountry_fragmentDirections.actionFromcountryFragmentToSpeechtotext(fromcountryname,fromcountryflag,countrylife.countryname,countrylife.flagicon)
        navController.navigate(action)
    }




}