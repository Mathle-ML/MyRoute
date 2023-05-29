package com.myroute.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myroute.CustomAdapter
import com.myroute.MainActivity
import com.myroute.R

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
        MainActivity.toolbar.visibility = View.VISIBLE
        MainActivity.bottomBar.visibility = View.VISIBLE

        viewCont = inflater.inflate(R.layout.fragment_camiones, container, false)
        val recyclerView = viewCont.findViewById<RecyclerView>(R.id.recyclerViewCamiones)
        val adapter = CustomAdapter()

        /*val cv1 = viewCont.findViewById<CardView>(R.id.cv1)
        val cv2 = viewCont.findViewById<CardView>(R.id.cv2)
        val cv3 = viewCont.findViewById<CardView>(R.id.cv3)
        val cv4 = viewCont.findViewById<CardView>(R.id.cv4)

        cv1.setOnClickListener {
            MainActivity.mainContext.findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
            FragmentMap.routToGenerate = "C47-Panteon | Dos templos"
        }
        cv2.setOnClickListener {
            MainActivity.mainContext.findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
            FragmentMap.routToGenerate = "C47-Kilometro 13 | Dos templos"
        }
        cv3.setOnClickListener {
            MainActivity.mainContext.findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
            FragmentMap.routToGenerate = "C47-Panteon | Kilometro 13"
        }
        cv4.setOnClickListener {
            MainActivity.mainContext.findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
            FragmentMap.routToGenerate = "C47_Kilometro 13 | Kilometro 13"
        }*/

        recyclerView.layoutManager = LinearLayoutManager(MainActivity.mainContext)
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
    }

}