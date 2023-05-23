package com.myroute

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.myroute.dbmanager.DBManager
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred

open class MainActivity : AppCompatActivity() {
    // la instancia que maneja la corrutina, esta debe estar inicializada aqui
    var coroutineManager: Deferred<Unit>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Metodo que instancia la clase
        super.onCreate(savedInstanceState)

        /* Cualquiera de las dos clases que hice deben estar instanciadas despues de onCreate,
        *  ya que si no, dara error por que no se ah instanciado correctamente la clase */
        val mapManager = MapManager(this)
        val dbManager = DBManager(this)

        setContentView(R.layout.activity_main)

        // No hay necesidad de que ejecutes dos veces generate map, aun que no dara error si lo haces
        mapManager.generateMap()

        // Recuerda usar que generateRoute() te devuelve falso o verdadero, por si lo llegas a necesitar
        mapManager.generateRoute("C47-Panteon | DosTemplos")

        /* El ID esta conformado por el nmero de ruta "C47", Nombre de sub ruta o nombre identificador
        *  "Panteon" y el destino "Dos templos"*/


        // Este metodo aun no lo eh testeado pero se supone deberia de funcionar
        val routes = dbManager.getAllRoutes()
        for (i in routes.indices){
            var ruta = routes.get(i)
            mapManager.generateRoute(ruta.getIDRoute()!!)
        }
    }
}