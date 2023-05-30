package com.myroute.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.myroute.MainActivity
import com.myroute.R

class FragmentMenu : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("MyRoute:Info", "Aca se ejecuta menu")
        MainActivity.toolbar.visibility = View.GONE
        MainActivity.bottomBar.visibility = View.GONE

        val menuView = inflater.inflate(R.layout.fragment_menu, container, false)

        val btnPanel = menuView.findViewById<Button>(R.id.btnPanel)
        val btnAbout = menuView.findViewById<Button>(R.id.btnAbout)
        val btnTerms = menuView.findViewById<Button>(R.id.btnTerms)
        val btnPrivacity = menuView.findViewById<Button>(R.id.btnPrivacity)

        btnPanel.setOnClickListener {
            when{
                MainActivity.isFragmentTrenes -> MainActivity.mainContext.findNavController(R.id.mainContainer)
                                                    .navigate(R.id.action_fragmentMenu_to_fragmentTrenes)
                MainActivity.isFragmentCamiones -> MainActivity.mainContext.findNavController(R.id.mainContainer)
                    .navigate(R.id.action_fragmentMenu_to_fragmentCamiones)
                MainActivity.isFragmentMap -> MainActivity.mainContext.findNavController(R.id.mainContainer)
                    .navigate(R.id.action_fragmentMenu_to_fragmentMap)
            }
        }
        btnAbout.setOnClickListener {
            MainActivity.toolbar.visibility = View.VISIBLE
            MainActivity.mainContext.findNavController(R.id.mainContainer)
                .navigate(R.id.action_fragmentMenu_to_fragmentAbout)
        }
        btnTerms.setOnClickListener {
            MainActivity.toolbar.visibility = View.VISIBLE
            MainActivity.mainContext.findNavController(R.id.mainContainer)
                .navigate(R.id.action_fragmentMenu_to_fragmentTerms)
        }
        btnPrivacity.setOnClickListener {
            MainActivity.toolbar.visibility = View.VISIBLE
            MainActivity.mainContext.findNavController(R.id.mainContainer)
                .navigate(R.id.action_fragmentMenu_to_fragmentPrivacity)
        }


        return menuView
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTrenes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}