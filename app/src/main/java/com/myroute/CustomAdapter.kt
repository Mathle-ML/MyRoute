package com.myroute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.myroute.dbmanager.DBManager
import com.myroute.fragments.FragmentMap
import java.lang.reflect.Array.getLength
import java.util.regex.Pattern

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val dbManager = DBManager(mainContext)
    val rutas = dbManager.getAllRoutes()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.recycle_view, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var str = rutas[i].getIDRoute().split("|")
        viewHolder.itemTitle.text = str[0]
        viewHolder.itemText.text = "Camion ${str[0]} \nRuta ${str[1]} \nColonias: ejemplo"
        viewHolder.itemImage.setImageResource(R.drawable.front_of_bus)
        viewHolder.itemButton.setOnClickListener(){
            mainContext.findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
            MainActivity.isFragmentMap = true
            MainActivity.isFragmentCamiones = false
            MainActivity.btnCamiones.setSelected(false)
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

        init {
            itemImage = itemView.findViewById(R.id.image)
            itemTitle = itemView.findViewById(R.id.title)
            itemText = itemView.findViewById(R.id.text)
            itemButton = itemView.findViewById(R.id.button1)
        }
    }

    companion object {
        lateinit var mainContext: MainActivity
    }
}