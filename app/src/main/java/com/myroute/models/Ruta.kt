package com.myroute.models

import android.graphics.Color
import com.myroute.R
import org.osmdroid.util.GeoPoint

class Ruta(
    private val idRoute : String,
    private val refPointsArray : ArrayList<GeoPoint>,
    private val refStopsArray : ArrayList<GeoPoint>,
    private val color : String,
    private val type : String)
{
    fun getIDRoute() : String{
        return idRoute
    }

    fun getRouteType() : String{
        return type
    }

    fun getRefPoints() : ArrayList<GeoPoint>{
        return refPointsArray
    }
    fun getRefStops() : ArrayList<GeoPoint>{
        return refStopsArray
    }
    fun getColor() : Int{
        return when(this.color){
            "green" -> Color.rgb(11, 189, 28)
            "green_train" -> Color.rgb(1, 114, 86)
            "brown" -> Color.rgb(118, 34, 46)
            "pink" -> Color.rgb(214, 2, 128)
            "red" -> Color.rgb(195, 27, 16)
            "zd" -> R.color.grey
            else -> 0
        }
    }
    companion object{
        val TYPE_BUS = "bus"
        val TYPE_TRAIN = "train"
    }
}
