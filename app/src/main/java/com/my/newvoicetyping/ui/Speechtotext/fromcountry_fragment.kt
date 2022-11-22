package com.my.newvoicetyping.ui.Speechtotext

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.newvoicetyping.R
import com.my.newvoicetyping.databinding.FragmentFromcountryFragmentBinding


class fromcountry_fragment : Fragment(),countryitemclicklistner, SearchView.OnQueryTextListener {
    private var _binding: FragmentFromcountryFragmentBinding? = null
    lateinit var navController: NavController
    private val binding get() = _binding!!
    lateinit var adapter:countryrvadapter
    private val mToolbar: Toolbar? = null
    lateinit var mSearchView : SearchView
    lateinit var mSearchString : String
    val args:fromcountry_fragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFromcountryFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setHasOptionsMenu(true)
        mToolbar!!.inflateMenu(R.menu.search_menu)
        setupMenu()
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

        binding.rvcountry.layoutManager= LinearLayoutManager(requireContext())
        binding.rvcountry.adapter=adapter

        return root
    }

    private fun setupMenu() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun oncountryitemclicked(countrylife: countrylife) {
        val tocountryname=args.tocountryname
        val tocountryflag=args.tocountryflag
        val action =fromcountry_fragmentDirections.actionFromcountryFragmentToSpeechtotext(countrylife.countryname,countrylife.flagicon,tocountryname,tocountryflag)
        navController.navigate(action)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
 //       super.onCreateOptionsMenu(menu, inflater)

        ///////

      //  val inflater: MenuInflater = in
      //  inflater.inflate(R.menu.chat_screen_menu, menu)

     /*   val searchManager = getSystemService<Any>(Context.SEARCH_SERVICE) as SearchManager?
        mSearchView =
            MenuItemCompat.getActionView(menu.findItem(R.id.action_search)) as SearchView

        mSearchView.setSearchableInfo(searchManager!!.getSearchableInfo(requireActivity().componentName))
        mSearchView.isIconifiedByDefault = false
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    mSearchString = newText
                    //doFilterAsync(mSearchString);
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Test1",
                        Toast.LENGTH_LONG
                    ).show()
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    mSearchString = query
                    //doFilterAsync(mSearchString);
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Test2",
                        Toast.LENGTH_LONG
                    ).show()
                    return true
                }
            }
        mSearchView.setOnQueryTextListener(queryTextListener)

        return true*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                searchItems()
            }
        }
        return true
    }

    private fun searchItems() {
        TODO("Not yet implemented")
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        TODO("Not yet implemented")
    }
}