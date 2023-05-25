package com.myroute.mapmanager

import com.myroute.MainActivity
import com.myroute.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import androidx.lifecycle.lifecycleScope
import com.myroute.dbmanager.DBManager
import com.myroute.models.Ruta
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapManager(private val context: MainActivity){
    private lateinit var mapView: MapView
    private val dbManager : DBManager = DBManager(context)


    // Funcion para generar el mapa (la vista)
    fun generateMap(){
        // Inicializando osmdroid
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

        // Buscando la etiqueta
        mapView = context.findViewById(R.id.mapView)

        // Configuracion inicial
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(14.0)
    }

    /*
    * #Funcion para generar el overlay de la ruta
    *    Devuelve TRUE si se logro completar la operacion
    *    Devuelve FALSE si no se logro completar la operacion
    */
    fun generateRoute(id_route: String) : Boolean{
        // Obtenemos la ruta y retorna FALSE si no se econtro la ruta
        val route : Ruta = dbManager.getRoute(id_route) ?: return false

        /* Verifica si la corrutina es nula o esta completada para poder iniciar una nueva corrutina
        *  en caso de que ya se este ejecutando una corrutina retornara FALSE                        */
        if (context.coroutineManager == null || context.coroutineManager!!.isCompleted){context.coroutineManager = context.lifecycleScope.async(Dispatchers.IO) {

            //Eliminamos los overlays de la anterior ruta
            mapView.overlays.clear()
            mapView.invalidate()

            // Generacion del overlay de la ruta
            val roadManager = OSRMRoadManager(context ,OSRMRoadManager.MEAN_BY_CAR)
            val road = roadManager.getRoad(route.getRefPoints())
            val roadOverlay = RoadManager.buildRoadOverlay(road, route.getColor(), 15F)

            // Añado primero el overlay de la ruta
            mapView.overlays.add(roadOverlay)

            // Generacion de iconos
            val nodeIcon = context.resources.getDrawable(R.drawable.icono_parada)
            for (i in route.getRefStops()!!.indices) {
                val nodeMarker = Marker(mapView)
                nodeMarker.position = route.getRefStops()!![i]
                nodeMarker.icon = nodeIcon
                nodeMarker.title = "Step $i"
                //Se añade cada icono
                mapView.overlays.add(nodeMarker)
            }

            // Refrescamos el mapa
            mapView.invalidate()

            // Centramos el mapa
            mapView.controller.setCenter(road.mBoundingBox.centerWithDateLine)
            mapView.controller.setZoom(14.0)

        }}else{return false}

        // Retornamos TRUE por operacion exitosa
        return true
    }
}