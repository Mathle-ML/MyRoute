package com.myroute.dbmanager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.myroute.MainActivity
import com.myroute.models.Colonia
import com.myroute.models.Ruta
import org.osmdroid.util.GeoPoint

class DBManager(private val context: MainActivity) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //Informacion de la base de datos
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db.sqlite3"
        private const val TABLE_NAME_RUTAS = "rutas"
        private const val TABLE_NAME_COLONIAS = "colonias"
        private const val COLUMN_ID_ROUTE = "id_route"
        private const val COLUMN_REF_POINTS = "ref_points"
        private const val COLUMN_REF_STOPS = "ref_stops"
        private const val COLUMN_COLOR = "color"
        private const val COLUMN_ID_COLN = "id_coln"
        private const val COLUMN_REF_ROUTS = "ref_routs"
        private const val COLUMN_COLN_RADIO = "coln_radio"
        private const val COLUMN_COLN_COLIN = "coln_colin"
    }

    //--------Metodos para el manejo de la base de datos--------//
    override fun onCreate(db: SQLiteDatabase?) {

        val createRutasTable = "CREATE TABLE $TABLE_NAME_RUTAS ($COLUMN_ID_ROUTE TEXT PRIMARY KEY, $COLUMN_REF_POINTS TEXT, $COLUMN_REF_STOPS TEXT, $COLUMN_COLOR TEXT)"
        val createColoniasTable = "CREATE TABLE $TABLE_NAME_COLONIAS ($COLUMN_ID_COLN TEXT PRIMARY KEY, $COLUMN_REF_ROUTS TEXT, $COLUMN_COLN_RADIO REAL, $COLUMN_COLN_COLIN TEXT)"
        db?.execSQL(createRutasTable)
        db?.execSQL(createColoniasTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_RUTAS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_COLONIAS")
        onCreate(db)
    }
    //----------------------------------------------------------//

    //-------------Metodos para obtener los modelos-------------//
    fun addRoute(idRoute: String, refPoints: ArrayList<DoubleArray>, refStops: ArrayList<DoubleArray>, color: String) {
        val db = this.writableDatabase

        // Checamos si la ruta existe
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_RUTAS WHERE $COLUMN_ID_ROUTE=?", arrayOf(idRoute))
        if (cursor.count == 0) {
            // Si no existe insertamos la ruta en la tabla
            val values = ContentValues()
            values.put(COLUMN_ID_ROUTE, idRoute)

            // Convertimos los puntos de referencia a string
            val refPointsStr = refPoints.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_POINTS, refPointsStr)

            // Convertimos los puntos de parada a string
            val refStopsStr = refStops.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_STOPS, refStopsStr)

            values.put(COLUMN_COLOR, color)

            db.insert(TABLE_NAME_RUTAS, null, values)
        } else {
            // Si existe solo actualizamos la ruta
            val values = ContentValues()

            // Convertimos los puntos de referencia a string
            val refPointsStr = refPoints.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_POINTS, refPointsStr)

            // Convertimos los puntos de parada a string
            val refStopsStr = refStops.joinToString("|") { point -> "${point[0]},${point[1]}" }
            values.put(COLUMN_REF_STOPS, refStopsStr)

            values.put(COLUMN_COLOR, color)

            db.update(TABLE_NAME_RUTAS, values, "$COLUMN_ID_ROUTE=?", arrayOf(idRoute))
        }

        cursor.close()
        db.close()
    }
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

        return Ruta(idRoute, refPointsArray, refStopsArray, color)
    }

    @SuppressLint("Range")
    fun getAllRoutes(): ArrayList<Ruta>? {


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

    @SuppressLint("Range")
    fun getColonia(idColn: String): Colonia? {

        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME_COLONIAS WHERE $COLUMN_ID_COLN=?", arrayOf(idColn))
        if (cursor.count == 0) {
            cursor.close()
            db.close()
            return null
        }

        cursor.moveToFirst()

        val refRoutsStr = cursor.getString(cursor.getColumnIndex(COLUMN_REF_ROUTS))
        val colnColinStr = cursor.getString(cursor.getColumnIndex(COLUMN_COLN_COLIN))
        val colnRadio = cursor.getDouble(cursor.getColumnIndex(COLUMN_COLN_RADIO))

        cursor.close()
        db.close()

        val refRoutsArray = refRoutsStr.split(",").toTypedArray()
        val colnColinArray = colnColinStr.split(",").toTypedArray()

        return Colonia(idColn, refRoutsArray, colnColinArray, colnRadio)
    }
    //----------------------------------------------------------//
}