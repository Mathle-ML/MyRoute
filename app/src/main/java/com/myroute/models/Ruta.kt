package com.myroute.models

import android.graphics.Color
import org.osmdroid.util.GeoPoint

class Ruta(
    private val idRoute : String,
    private val refPointsArray : ArrayList<GeoPoint>,
    private val refStopsArray : ArrayList<GeoPoint>,
    private val color : Int,)
{
    fun getIDRoute() : String?{
        return idRoute
    }
    fun getRefPoints() : ArrayList<GeoPoint>?{
        return refPointsArray
    }
    fun getRefStops() : ArrayList<GeoPoint>?{
        return refStopsArray
    }
    fun getColor() : Int{
        return color
    }
}
