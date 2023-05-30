package com.myroute.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.fragments.adapters.CustomAdapter

class FragmentCamiones : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewCont = inflater.inflate(R.layout.fragment_camiones, container, false)
        val recyclerView = viewCont.findViewById<RecyclerView>(R.id.recyclerViewCamiones)
        val adapter = CustomAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Actualizar visibilidad de la barra de herramientas y la barra inferior
        MainActivity.toolbar.visibility = View.VISIBLE
        MainActivity.bottomBar.visibility = View.VISIBLE

        return viewCont
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) = FragmentCamiones().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}