package com.myroute.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.dbmanager.DBManager
import com.myroute.models.Ruta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class FragmentMap : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewCont = inflater.inflate(R.layout.fragment_map, container, false)

        generateMap()
        generateRoute()
        return viewCont
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMap().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


        private lateinit var viewCont: View
        private lateinit var mapView: MapView
        private lateinit var dbManager : DBManager
        lateinit var id_route : String
        lateinit var maincontext: MainActivity
        lateinit var roadOverlay: Polyline
        lateinit var road: Road

        fun generateMap(){
            // Buscando la etiqueta
            mapView = viewCont.findViewById(R.id.mapView)

            // Configuracion inicial
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.setBuiltInZoomControls(false)
            mapView.controller.setZoom(14.0)
        }

        fun generateRoute() : Boolean{
            if (!::id_route.isInitialized || id_route == null)return false
            Log.i("MyRoute:Info", "Ruta a generar $id_route")
            dbManager = DBManager(maincontext)
            val route : Ruta = dbManager.getRoute(id_route) ?: return false
            id_route == null

            mapView = viewCont.findViewById(R.id.mapView)

            // Configuracion inicial
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.setBuiltInZoomControls(false)
            mapView.controller.setZoom(14.0)

                mapView.overlays.clear()
                mapView.invalidate()
                GlobalScope.launch {
                    val roadManager = OSRMRoadManager(maincontext , OSRMRoadManager.MEAN_BY_CAR)
                    road = roadManager.getRoad(route.getRefPoints())
                    roadOverlay = RoadManager.buildRoadOverlay(road, route.getColor(), 15F)
                }

                while (!::roadOverlay.isInitialized || roadOverlay == null){}

                mapView.overlays.add(roadOverlay)
                val nodeIcon = maincontext.resources.getDrawable(R.drawable.icono_parada)
                for (i in route.getRefStops()!!.indices) {
                    val nodeMarker = Marker(mapView)
                    nodeMarker.position = route.getRefStops()!![i]
                    nodeMarker.icon = nodeIcon
                    nodeMarker.title = "Step $i"
                    mapView.overlays.add(nodeMarker)
                }

                mapView.invalidate()

                mapView.controller.setCenter(road.mBoundingBox.centerWithDateLine)
                mapView.controller.setZoom(14.0)

            roadOverlay == null
            return true
        }
    }
}