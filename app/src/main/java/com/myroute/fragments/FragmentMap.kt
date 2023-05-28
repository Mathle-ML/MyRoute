package com.myroute.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.dbmanager.DBManager
import com.myroute.models.Ruta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
@Suppress("DEPRECATION")
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
        return inflater.inflate(R.layout.fragment_map, container, false).also {
            initView(it)
        }
    }

    private fun initView(view: View) {
        val mapView = view.findViewById<MapView>(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.setBuiltInZoomControls(false)
        mapView.controller.setZoom(14.0)

        generateRoute(mapView)
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

        @SuppressLint("StaticFieldLeak")
        lateinit var mainContext: MainActivity
        var routToGenerate: String? = null
        private var coroutineManager: Job? = null

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun generateRoute(mapView: MapView) {
            coroutineManager?.cancel()
            coroutineManager = CoroutineScope(Dispatchers.Main).launch {
                val dbManager = DBManager(mainContext)
                val route: Ruta = dbManager.getRoute(routToGenerate ?: return@launch) ?: return@launch
                val road = withContext(Dispatchers.IO) {
                    val roadManager = OSRMRoadManager(mainContext, OSRMRoadManager.MEAN_BY_CAR)
                    roadManager.getRoad(route.getRefPoints())
                }
                val roadOverlay = RoadManager.buildRoadOverlay(road, route.getColor(), 15F)

                mapView.overlays.clear()
                mapView.overlays.add(roadOverlay)
                val nodeIcon = mainContext.resources.getDrawable(R.drawable.icono_parada)
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
            }
        }
    }
}







