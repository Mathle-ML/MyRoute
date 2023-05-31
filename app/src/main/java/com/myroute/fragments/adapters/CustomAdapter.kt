package com.myroute.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.dbmanager.DBManager
import com.myroute.fragments.FragmentMap
import com.myroute.models.Ruta

class CustomAdapter(whereAre: String,private val type: String): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val dbManager = DBManager(MainActivity.mainContext)
    val rutas = dbManager.getAllRoutes(type)
    var whereAre = whereAre

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = if(type == Ruta.TYPE_BUS){
            LayoutInflater.from(viewGroup.context).inflate(R.layout.recycle_view_camiones, viewGroup, false)
        }else{
            LayoutInflater.from(viewGroup.context).inflate(R.layout.recycle_view_train, viewGroup, false)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var str = rutas[i].getIDRoute().split("|")
        viewHolder.itemTitle.text = str[0]
        viewHolder.itemText.text = "Destino: ${str[1]}"
        viewHolder.color.setBackgroundColor(rutas[i].getColor())
        viewHolder.itemImage.setColorFilter(rutas[i].getColor())
        viewHolder.itemButton.setOnClickListener(){
            when(whereAre){
                "camiones" -> {
                    MainActivity.mainContext.findNavController(R.id.mainContainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                    MainActivity.isFragmentCamiones = false
                    MainActivity.btnCamiones.setSelected(false)
                }
                "trenes" -> {
                    MainActivity.mainContext.findNavController(R.id.mainContainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                    MainActivity.isFragmentTrenes = false
                    MainActivity.btnTrenes.setSelected(false)
                }
            }

            MainActivity.isFragmentMap = true
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
        var cardView: CardView

        init {
            cardView = itemView.findViewById(R.id.cardView)
            color = itemView.findViewById(R.id.color)
            itemImage = itemView.findViewById(R.id.image)
            itemTitle = itemView.findViewById(R.id.title)
            itemText = itemView.findViewById(R.id.text)
            itemButton = itemView.findViewById(R.id.button1)
        }
    }

    companion object{
        val CAMIONES = "camiones"
        val TRENES = "trenes"
    }
}