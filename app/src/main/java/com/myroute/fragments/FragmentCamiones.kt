package com.myroute.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.myroute.R
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myroute.CustomAdapter
import com.myroute.MainActivity

class FragmentCamiones : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewCont: View

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
        viewCont = inflater.inflate(R.layout.fragment_camiones, container, false)

        val recyclerView = viewCont.findViewById<RecyclerView>(R.id.recyclerViewCamiones)
        val adapter = CustomAdapter()

        recyclerView.layoutManager = LinearLayoutManager(mainContext)
        recyclerView.adapter = adapter

        return viewCont
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCamiones().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        lateinit var mainContext: MainActivity
    }

}