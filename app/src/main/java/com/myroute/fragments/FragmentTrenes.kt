package com.myroute.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.fragments.adapters.CustomAdapter
import com.myroute.models.Ruta

class FragmentTrenes : Fragment() {
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
        val viewCont = inflater.inflate(R.layout.fragment_trenes, container, false)
        val recyclerView = viewCont.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter(CustomAdapter.TRENES, Ruta.TYPE_TRAIN)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Actualizar visibilidad de la barra de herramientas y la barra inferior
        MainActivity.toolbar.visibility = View.VISIBLE
        MainActivity.bottomBar.visibility = View.VISIBLE

        return viewCont
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