package com.myroute.models

import android.graphics.Color
import org.osmdroid.util.GeoPoint

class Ruta(
    private val idRoute: String,
    private val refPointsArray: ArrayList<GeoPoint>,
    private val refStopsArray: ArrayList<GeoPoint>,
    private val color: String,
    private val type: String
) {
    fun getIDRoute(): String = idRoute

    fun getRouteType(): String = type

    fun getRefPoints(): ArrayList<GeoPoint> = refPointsArray

    fun getRefStops(): ArrayList<GeoPoint> = refStopsArray

    fun getColor(): Int {
        return when (color) {
            "green" -> Color.rgb(11, 189, 28)
            else -> Color.BLACK
        }
    }
}
