package com.myroute.dbmanager

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.myroute.models.Ruta
import org.osmdroid.util.GeoPoint

class DBManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //Informacion de la base de datos
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db.sqlite3"

        private const val TABLE_NAME_RUTAS = "rutas"

        private const val COLUMN_ID_ROUTE = "id_route"
        private const val COLUMN_REF_POINTS = "ref_points"
        private const val COLUMN_REF_STOPS = "ref_stops"
        private const val COLUMN_COLOR = "color"
        private const val COLUMN_TYPE = "tipo"
    }

    //--------Metodos para el manejo de la base de datos--------//
    override fun onCreate(db: SQLiteDatabase?) {
        val createRutasTable = "CREATE TABLE $TABLE_NAME_RUTAS ($COLUMN_ID_ROUTE TEXT PRIMARY KEY, $COLUMN_REF_POINTS TEXT, $COLUMN_REF_STOPS TEXT, $COLUMN_COLOR TEXT, $COLUMN_TYPE TEXT)"
        db?.execSQL(createRutasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_RUTAS")
        onCreate(db)
    }
    //----------------------------------------------------------//

    //-------------Metodos para obtener los modelos-------------//

    @SuppressLint("Range")
    fun getRoute(idRoute: String): Ruta? {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_REF_POINTS, $COLUMN_REF_STOPS, $COLUMN_COLOR FROM $TABLE_NAME_RUTAS WHERE $COLUMN_ID_ROUTE=?", arrayOf(idRoute))

        if (cursor.count == 0) {
            cursor.close()
            db.close()
            return null
        }

        cursor.moveToFirst()

        val refPointsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_POINTS))
        val refStopsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_STOPS))
        val color = cursor.getString(cursor.getColumnIndex(COLUMN_COLOR))

        //Codigo provicional
        var type: String
        type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
        if(type == null)type = "none"

        cursor.close()
        db.close()
    
        // Convertimos los string a array de GeoPoint
        val mutablepointsList = refPointsStr.split("|").map { pointStr ->
            val latLngStr = pointStr.split(",")
            val lat = latLngStr[0].toDouble()
            val lng = latLngStr[1].toDouble()
            GeoPoint(lat, lng)
        }.toMutableList()

        val mutablestopsList = refStopsStr.split("|").map { pointStr ->
            val latLngStr = pointStr.split(",")
            val lat = latLngStr[0].toDouble()
            val lng = latLngStr[1].toDouble()
            GeoPoint(lat, lng)
        }.toMutableList()

        val refPointsArray: ArrayList<GeoPoint> = ArrayList(mutablepointsList)
        val refStopsArray: ArrayList<GeoPoint> = ArrayList(mutablestopsList)

        return Ruta(idRoute, refPointsArray, refStopsArray, color, type)
    }

    @SuppressLint("Range")
    fun getAllRoutes(): ArrayList<Ruta> {
        val routes = ArrayList<Ruta>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_ID_ROUTE FROM $TABLE_NAME_RUTAS", null)

        if (cursor.moveToFirst()) {
            do {
                val idRoute = cursor.getString(cursor.getColumnIndex(COLUMN_ID_ROUTE))
                val route = getRoute(idRoute) // Utiliza el método existente getRoute para obtener cada ruta individualmente
                route?.let {
                    routes.add(it)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return routes
    }
    //----------------------------------------------------------//
}