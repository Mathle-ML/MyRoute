package com.myroute.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.dbmanager.DBManager
import com.myroute.fragments.FragmentMap

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val dbManager = DBManager(MainActivity.mainContext)
    val rutas = dbManager.getAllRoutes()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycle_view, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var str = rutas[i].getIDRoute().split("|")
        Log.i("MyRoute:Info", "${str[0]}")
        viewHolder.itemTitle.text = str[0]
        viewHolder.itemText.text = "Destino: ${str[1]}"
        viewHolder.color.setBackgroundColor(rutas[i].getColor())
        viewHolder.itemButton.setOnClickListener(){
            MainActivity.mainContext.findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
            MainActivity.btnMap.setSelected(true)
            FragmentMap.routToGenerate = rutas[i].getIDRoute()
        }
    }

    override fun getItemCount(): Int {
        return rutas.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemText: TextView
        var itemButton: Button
        var color: ImageView

        init {
            color = itemView.findViewById(R.id.color)
            itemImage = itemView.findViewById(R.id.image)
            itemTitle = itemView.findViewById(R.id.title)
            itemText = itemView.findViewById(R.id.text)
            itemButton = itemView.findViewById(R.id.button1)
        }
    }
}