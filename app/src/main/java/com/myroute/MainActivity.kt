package com.myroute

import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.myroute.dbmanager.DBCheckUpdates
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    // la instancia que maneja la corrutina, esta debe estar inicializada aqui
    var coroutineManager: Deferred<Unit>? = null
    // variables de acceso a las propiedades del actualizador de la base de datos y majeo de mapa
    lateinit var dbupdate: DBCheckUpdates
    lateinit var mapManager: MapManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Metodo que instancia la clase
        super.onCreate(savedInstanceState)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Inicializacion de variables (Las clases que yo hice)
        dbupdate = DBCheckUpdates(this)
        mapManager = MapManager(this)

        setContentView(R.layout.activity_main)

        /*
        # Cualquiera de las dos clases que hice deben estar instanciadas despues de onCreate,
        # ya que si no, dara error por que no se ah instanciado correctamente la clase
        val mapManager = MapManager(this)
        val dbManager = DBManager(this)

        # No hay necesidad de que ejecutes dos veces generate map, aun que no dara error si lo haces
        mapManager.generateMap()

        # Recuerda usar que generateRoute() te devuelve falso o verdadero, por si lo llegas a necesitar
        mapManager.generateRoute("C47-Panteon | DosTemplos")

        # El ID esta conformado por el nmero de ruta "C47", Nombre de sub ruta o nombre identificador
        # "Panteon" y el destino "Dos templos"


        # Este metodo aun no lo eh testeado pero se supone deberia de funcionar
        val routes = dbManager.getAllRoutes()
        for (i in routes!!.indices){
            var ruta = routes.get(i)
            mapManager.generateRoute(ruta.getIDRoute()!!)
        }
        * */

        var isFragmentMap = true
        var isFragmentCamiones = false
        var isFragmentTrenes = false

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        btnMap.setSelected(true)
        btnMap.setOnClickListener{
            if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                isFragmentMap = true
                isFragmentCamiones = false
                btnCamiones.setSelected(false)
            }else if(isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                isFragmentMap = true
                isFragmentTrenes = false
                btnTrenes.setSelected(false)
            }
            btnMap.setSelected(true)
        }
        btnCamiones.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentMap = false
                btnMap.setSelected(false)
            }else if(isFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                isFragmentCamiones = true
                isFragmentTrenes = false
                btnTrenes.setSelected(false)
            }
            btnCamiones.setSelected(true)
        }
        btnTrenes.setOnClickListener{
            if(isFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentMap = false
                btnMap.setSelected(false)
            }else if(isFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                isFragmentTrenes = true
                isFragmentCamiones = false
                btnCamiones.setSelected(false)
            }
            btnTrenes.setSelected(true)
        }

    }
}
