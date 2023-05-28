package com.myroute.models

import android.graphics.Color
import org.osmdroid.util.GeoPoint

class Ruta(
    private val idRoute: String,
    private val refPointsArray: ArrayList<GeoPoint>,
    private val refStopsArray: ArrayList<GeoPoint>,
    private val color: String,
    private val type: String)
{
    fun getIDRoute() : String{
        return idRoute
    }
    fun getRouteType() : String{
        return type
    }
    fun getRefPoints() : ArrayList<GeoPoint>?{
        return refPointsArray
    }
    fun getRefStops() : ArrayList<GeoPoint>?{
        return refStopsArray
    }
    fun getColor() : Int{
        var color = 0
        when(this.color){
            "green" ->{
                color = Color.rgb(11, 189, 28)
            }
        }
        return color
    }
}
