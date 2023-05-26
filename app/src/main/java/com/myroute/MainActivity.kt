package com.myroute

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.myroute.dbmanager.DBCheckUpdates
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    // la instancia que maneja la corrutina, esta debe estar inicializada aqui
    var coroutineManager: Deferred<Unit>? = null
    // variables de acceso a las propiedades del actualizador de la base de datos y majeo de mapa
    lateinit var dbupdate: DBCheckUpdates
    lateinit var mapManager: MapManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Metodo que instancia la clase
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.hide()
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

        var IsFragmentMap = true
        var IsFragmentCamiones = false
        var IsFragmentTrenes = false

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        val btnMap: ImageButton = findViewById(R.id.btnMap)
        btnMap.setOnClickListener{
            if(IsFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentMap)
                IsFragmentMap = true
                IsFragmentCamiones = false
            }else if(IsFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentMap)
                IsFragmentMap = true
                IsFragmentTrenes = false
            }
        }
        btnCamiones.setOnClickListener{
            if(IsFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentCamiones)
                IsFragmentCamiones = true
                IsFragmentMap = false
            }else if(IsFragmentTrenes){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentTrenes_to_fragmentCamiones)
                IsFragmentCamiones = true
                IsFragmentTrenes = false
            }
        }
        btnTrenes.setOnClickListener{
            if(IsFragmentMap){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentMap_to_fragmentTrenes)
                IsFragmentTrenes = true
                IsFragmentMap = false
            }else if(IsFragmentCamiones){
                findNavController(R.id.mainConainer).navigate(R.id.action_fragmentCamiones_to_fragmentTrenes)
                IsFragmentTrenes = true
                IsFragmentCamiones = false
            }
        }

    }
}
