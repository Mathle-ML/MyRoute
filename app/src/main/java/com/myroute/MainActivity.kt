package com.myroute

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.myroute.dbmanager.DBCheckUpdates
import com.myroute.dbmanager.DBManager
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred

class MainActivity : AppCompatActivity() {
    // la instancia que maneja la corrutina, esta debe estar inicializada aqui
    //var coroutineManager: Deferred<Unit>? = null
    // variables de acceso a las propiedades del actualizador de la base de datos y majeo de mapa
    //lateinit var dbupdate: DBCheckUpdates
    //lateinit var mapManager: MapManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Metodo que instancia la clase
        super.onCreate(savedInstanceState)

        //Inicializacion de variables (Las clases que yo hice)
        //dbupdate = DBCheckUpdates(this)
        //mapManager = MapManager(this)

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
        *

        val btnCamiones: ImageButton = findViewById(R.id.btnCamiones)
        btnCamiones.setOnClickListener{
            startActivity(Intent(this, Camiones::class.java))
        }
        val btnTrenes: ImageButton = findViewById(R.id.btnTrenes)
        btnTrenes.setOnClickListener{
            startActivity(Intent(this, Trenes::class.java))
        }*/
        val c15_zapotlanejo_elSalto_refPoints = arrayListOf(
            doubleArrayOf(20.55914275584629, -103.30772558615499),
            doubleArrayOf(20.560048390592222, -103.31068778253528),
            doubleArrayOf(20.561455518802553, -103.31605264362955),
            doubleArrayOf(20.562864375202512, -103.31728109473943), // km13
            doubleArrayOf(20.562864375202512, -103.31728109473943), // km13
            doubleArrayOf(20.563257862752707, -103.31763834917437), // km13
            doubleArrayOf(20.564644257721255, -103.32111324207581), // km13
            doubleArrayOf(20.567210777347434, -103.32718577702886),
            doubleArrayOf(20.569567684731666, -103.3274109569584),
            doubleArrayOf(20.572864646219223, -103.32629364969466),
            doubleArrayOf(20.57475328280367, -103.32579902655363),
            doubleArrayOf(20.57729920838228, -103.33176587940031),
            doubleArrayOf(20.584484862861295, -103.33216963164695),
            doubleArrayOf(20.590876042610372, -103.32099556913957),
            doubleArrayOf(20.59021916768284, -103.32126363377182),
            doubleArrayOf(20.620183253454694, -103.32148951802951),
            doubleArrayOf(20.627472280347018, -103.32121837146536),
            doubleArrayOf(20.651504765703965, -103.3365921671546),
            doubleArrayOf(20.658700889685438, -103.344948708882),
            doubleArrayOf(20.662288227977093, -103.34840068108089),
            doubleArrayOf(20.66263924729825, -103.34966305754341),
            doubleArrayOf(20.66317189833746, -103.34954848746027),
            doubleArrayOf(20.66997769002872, -103.34727145653733),
            doubleArrayOf(20.670209102857704, -103.34666851706183)
        )
        val c47_panteon_dosTemplos_refPoints = arrayListOf(
            doubleArrayOf(20.55914275584629, -103.30772558615499),
            doubleArrayOf(20.560048390592222, -103.31068778253528),
            doubleArrayOf(20.561455518802553, -103.31605264362955),
            doubleArrayOf(20.562655959691323, -103.32159548075023),
            doubleArrayOf(20.564584522717208, -103.32128885743529),
            doubleArrayOf(20.567210777347434, -103.32718577702886),
            doubleArrayOf(20.569567684731666, -103.3274109569584),
            doubleArrayOf(20.572864646219223, -103.32629364969466),
            doubleArrayOf(20.57475328280367, -103.32579902655363),
            doubleArrayOf(20.57729920838228, -103.33176587940031),
            doubleArrayOf(20.584484862861295, -103.33216963164695),
            doubleArrayOf(20.590876042610372, -103.32099556913957),
            doubleArrayOf(20.59021916768284, -103.32126363377182),
            doubleArrayOf(20.620183253454694, -103.32148951802951),
            doubleArrayOf(20.627472280347018, -103.32121837146536),
            doubleArrayOf(20.651504765703965, -103.3365921671546),
            doubleArrayOf(20.658700889685438, -103.344948708882),
            doubleArrayOf(20.662288227977093, -103.34840068108089),
            doubleArrayOf(20.66263924729825, -103.34966305754341),
            doubleArrayOf(20.66317189833746, -103.34954848746027),
            doubleArrayOf(20.66997769002872, -103.34727145653733),
            doubleArrayOf(20.670209102857704, -103.34666851706183)
        )
        val c47_kilometro13_dosTemplos_refPoints = arrayListOf(
            doubleArrayOf(20.55914275584629, -103.30772558615499),
            doubleArrayOf(20.560048390592222, -103.31068778253528),
            doubleArrayOf(20.561455518802553, -103.31605264362955),
            doubleArrayOf(20.562864375202512, -103.31728109473943),
            doubleArrayOf(20.562864375202512, -103.31728109473943),
            doubleArrayOf(20.563257862752707, -103.31763834917437),
            doubleArrayOf(20.564644257721255, -103.32111324207581),
            doubleArrayOf(20.567210777347434, -103.32718577702886),
            doubleArrayOf(20.569567684731666, -103.3274109569584),
            doubleArrayOf(20.572864646219223, -103.32629364969466),
            doubleArrayOf(20.57475328280367, -103.32579902655363),
            doubleArrayOf(20.57729920838228, -103.33176587940031),
            doubleArrayOf(20.584484862861295, -103.33216963164695),
            doubleArrayOf(20.590876042610372, -103.32099556913957),
            doubleArrayOf(20.59021916768284, -103.32126363377182),
            doubleArrayOf(20.620183253454694, -103.32148951802951),
            doubleArrayOf(20.627472280347018, -103.32121837146536),
            doubleArrayOf(20.651504765703965, -103.3365921671546),
            doubleArrayOf(20.658700889685438, -103.344948708882),
            doubleArrayOf(20.662288227977093, -103.34840068108089),
            doubleArrayOf(20.66263924729825, -103.34966305754341),
            doubleArrayOf(20.66317189833746, -103.34954848746027),
            doubleArrayOf(20.66997769002872, -103.34727145653733),
            doubleArrayOf(20.670209102857704, -103.34666851706183)
        )
        val c47_panteon_kilometro13_refPoints = arrayListOf(
            doubleArrayOf(20.559102379790055, -103.3077286145877),
            doubleArrayOf(20.561575125179132, -103.31605544646254),
            doubleArrayOf(20.564634936644616, -103.32129087799353),
            doubleArrayOf(20.56724200729579, -103.32711814086845),
            doubleArrayOf(20.570109674740245, -103.32703220357602),
            doubleArrayOf(20.57286522530409, -103.32629209975222),
            doubleArrayOf(20.57470171247172, -103.32578124508578),
            doubleArrayOf(20.575217711120676, -103.32667942755624),
            doubleArrayOf(20.575397527278707, -103.32710851393624),
            doubleArrayOf(20.575716780186976, -103.3279907175905),
            doubleArrayOf(20.576226312106236, -103.32918241197184),
            doubleArrayOf(20.57727311104247, -103.33164715076012),
            doubleArrayOf(20.57793635418961, -103.33166913849959),
            doubleArrayOf(20.579856079262587, -103.33146121869628),
            doubleArrayOf(20.580619547484833, -103.33157032365342),
            doubleArrayOf(20.581067318562447, -103.33156128555281),
            doubleArrayOf(20.58205800150421, -103.33174795537391),
            doubleArrayOf(20.58408151265353, -103.33211978648406),
            doubleArrayOf(20.587346796568173, -103.32678796619683),
            doubleArrayOf(20.58966901609703, -103.32281330325704),
            doubleArrayOf(20.593299232100545, -103.32260691313454),
            doubleArrayOf(20.597055846534328, -103.32434648325597),
            doubleArrayOf(20.60548866485727, -103.31581338226596),
            doubleArrayOf(20.60929702542163, -103.31519835862117),
            doubleArrayOf(20.620151420661433, -103.32137410536437),
            doubleArrayOf(20.62746948732305, -103.32112809986315),
            doubleArrayOf(20.629108019319244, -103.32219885619757),
            doubleArrayOf(20.633188722144173, -103.32481965710862),
            doubleArrayOf(20.635452800565055, -103.32629429346699),
            doubleArrayOf(20.638144567746284, -103.32793212499435),
            doubleArrayOf(20.64102151296647, -103.32983006801445),
            doubleArrayOf(20.6434714383289, -103.33143567864693),
            doubleArrayOf(20.645844755019517, -103.33292956340239),
            doubleArrayOf(20.64827445684351, -103.33444822095227),
            doubleArrayOf(20.650330679388855, -103.33576184930892),
            doubleArrayOf(20.65783679185825, -103.34386555281473),
            doubleArrayOf(20.65899096102669, -103.34523684371155),
            doubleArrayOf(20.662111200989653, -103.34817753276889),
            doubleArrayOf(20.663097942153552, -103.34948165722594),
            doubleArrayOf(20.665955577086333, -103.34851136797974),
            doubleArrayOf(20.669357361599637, -103.34736447870982),
            doubleArrayOf(20.670215878676608, -103.34665578518664)
        )
        val c47_kilometro13_kilometro13_refPoints = arrayListOf(
            doubleArrayOf(20.559102379790055, -103.3077286145877),
            doubleArrayOf(20.561575125179132, -103.31605544646254),
            doubleArrayOf(20.56336871356066, -103.31783144176015),
            doubleArrayOf(20.564634936644616, -103.32129087799353),
            doubleArrayOf(20.56724200729579, -103.32711814086845),
            doubleArrayOf(20.570109674740245, -103.32703220357602),
            doubleArrayOf(20.57286522530409, -103.32629209975222),
            doubleArrayOf(20.57470171247172, -103.32578124508578),
            doubleArrayOf(20.575217711120676, -103.32667942755624),
            doubleArrayOf(20.575397527278707, -103.32710851393624),
            doubleArrayOf(20.575716780186976, -103.3279907175905),
            doubleArrayOf(20.576226312106236, -103.32918241197184),
            doubleArrayOf(20.57727311104247, -103.33164715076012),
            doubleArrayOf(20.57793635418961, -103.33166913849959),
            doubleArrayOf(20.579856079262587, -103.33146121869628),
            doubleArrayOf(20.580619547484833, -103.33157032365342),
            doubleArrayOf(20.581067318562447, -103.33156128555281),
            doubleArrayOf(20.58205800150421, -103.33174795537391),
            doubleArrayOf(20.58408151265353, -103.33211978648406),
            doubleArrayOf(20.587346796568173, -103.32678796619683),
            doubleArrayOf(20.58966901609703, -103.32281330325704),
            doubleArrayOf(20.593299232100545, -103.32260691313454),
            doubleArrayOf(20.597055846534328, -103.32434648325597),
            doubleArrayOf(20.60548866485727, -103.31581338226596),
            doubleArrayOf(20.60929702542163, -103.31519835862117),
            doubleArrayOf(20.620151420661433, -103.32137410536437),
            doubleArrayOf(20.62746948732305, -103.32112809986315),
            doubleArrayOf(20.629108019319244, -103.32219885619757),
            doubleArrayOf(20.633188722144173, -103.32481965710862),
            doubleArrayOf(20.635452800565055, -103.32629429346699),
            doubleArrayOf(20.638144567746284, -103.32793212499435),
            doubleArrayOf(20.64102151296647, -103.32983006801445),
            doubleArrayOf(20.6434714383289, -103.33143567864693),
            doubleArrayOf(20.645844755019517, -103.33292956340239),
            doubleArrayOf(20.64827445684351, -103.33444822095227),
            doubleArrayOf(20.650330679388855, -103.33576184930892),
            doubleArrayOf(20.65783679185825, -103.34386555281473),
            doubleArrayOf(20.65899096102669, -103.34523684371155),
            doubleArrayOf(20.662111200989653, -103.34817753276889),
            doubleArrayOf(20.663097942153552, -103.34948165722594),
            doubleArrayOf(20.665955577086333, -103.34851136797974),
            doubleArrayOf(20.669357361599637, -103.34736447870982),
            doubleArrayOf(20.670215878676608, -103.34665578518664)
        )
        val c47_kilometro13_dosTemplos_refStops = arrayListOf(
            doubleArrayOf(20.559102379790055, -103.3077286145877),
            doubleArrayOf(20.561575125179132, -103.31605544646254),
            doubleArrayOf(20.56336871356066, -103.31783144176015),
            doubleArrayOf(20.564634936644616, -103.32129087799353),
            doubleArrayOf(20.56724200729579, -103.32711814086845),
            doubleArrayOf(20.570109674740245, -103.32703220357602),
            doubleArrayOf(20.57286522530409, -103.32629209975222),
            doubleArrayOf(20.57470171247172, -103.32578124508578),
            doubleArrayOf(20.575217711120676, -103.32667942755624),
            doubleArrayOf(20.575397527278707, -103.32710851393624),
            doubleArrayOf(20.575716780186976, -103.3279907175905),
            doubleArrayOf(20.576226312106236, -103.32918241197184),
            doubleArrayOf(20.57727311104247, -103.33164715076012),
            doubleArrayOf(20.57793635418961, -103.33166913849959),
            doubleArrayOf(20.579856079262587, -103.33146121869628),
            doubleArrayOf(20.580619547484833, -103.33157032365342),
            doubleArrayOf(20.581067318562447, -103.33156128555281),
            doubleArrayOf(20.58205800150421, -103.33174795537391),
            doubleArrayOf(20.58408151265353, -103.33211978648406),
            doubleArrayOf(20.587346796568173, -103.32678796619683),
            doubleArrayOf(20.58966901609703, -103.32281330325704),
            doubleArrayOf(20.593299232100545, -103.32260691313454),
            doubleArrayOf(20.597055846534328, -103.32434648325597),
            doubleArrayOf(20.60548866485727, -103.31581338226596),
            doubleArrayOf(20.60929702542163, -103.31519835862117),
            doubleArrayOf(20.620151420661433, -103.32137410536437),
            doubleArrayOf(20.62746948732305, -103.32112809986315),
            doubleArrayOf(20.629108019319244, -103.32219885619757),
            doubleArrayOf(20.633188722144173, -103.32481965710862),
            doubleArrayOf(20.635452800565055, -103.32629429346699),
            doubleArrayOf(20.638144567746284, -103.32793212499435),
            doubleArrayOf(20.64102151296647, -103.32983006801445),
            doubleArrayOf(20.6434714383289, -103.33143567864693),
            doubleArrayOf(20.645844755019517, -103.33292956340239),
            doubleArrayOf(20.64827445684351, -103.33444822095227),
            doubleArrayOf(20.650330679388855, -103.33576184930892),
            doubleArrayOf(20.65783679185825, -103.34386555281473),
            doubleArrayOf(20.65899096102669, -103.34523684371155),
            doubleArrayOf(20.662111200989653, -103.34817753276889),
            doubleArrayOf(20.663097942153552, -103.34948165722594),
            doubleArrayOf(20.665955577086333, -103.34851136797974),
            doubleArrayOf(20.669357361599637, -103.34736447870982),
            doubleArrayOf(20.670215878676608, -103.34665578518664)
        )
        val c47_panteon_dosTemplos_refStops = arrayListOf(
            doubleArrayOf(20.559102379790055, -103.3077286145877),
            doubleArrayOf(20.561575125179132, -103.31605544646254),
            doubleArrayOf(20.564634936644616, -103.32129087799353),
            doubleArrayOf(20.56724200729579, -103.32711814086845),
            doubleArrayOf(20.570109674740245, -103.32703220357602),
            doubleArrayOf(20.57286522530409, -103.32629209975222),
            doubleArrayOf(20.57470171247172, -103.32578124508578),
            doubleArrayOf(20.575217711120676, -103.32667942755624),
            doubleArrayOf(20.575397527278707, -103.32710851393624),
            doubleArrayOf(20.575716780186976, -103.3279907175905),
            doubleArrayOf(20.576226312106236, -103.32918241197184),
            doubleArrayOf(20.57727311104247, -103.33164715076012),
            doubleArrayOf(20.57793635418961, -103.33166913849959),
            doubleArrayOf(20.579856079262587, -103.33146121869628),
            doubleArrayOf(20.580619547484833, -103.33157032365342),
            doubleArrayOf(20.581067318562447, -103.33156128555281),
            doubleArrayOf(20.58205800150421, -103.33174795537391),
            doubleArrayOf(20.58408151265353, -103.33211978648406),
            doubleArrayOf(20.587346796568173, -103.32678796619683),
            doubleArrayOf(20.58966901609703, -103.32281330325704),
            doubleArrayOf(20.593299232100545, -103.32260691313454),
            doubleArrayOf(20.597055846534328, -103.32434648325597),
            doubleArrayOf(20.60548866485727, -103.31581338226596),
            doubleArrayOf(20.60929702542163, -103.31519835862117),
            doubleArrayOf(20.620151420661433, -103.32137410536437),
            doubleArrayOf(20.62746948732305, -103.32112809986315),
            doubleArrayOf(20.629108019319244, -103.32219885619757),
            doubleArrayOf(20.633188722144173, -103.32481965710862),
            doubleArrayOf(20.635452800565055, -103.32629429346699),
            doubleArrayOf(20.638144567746284, -103.32793212499435),
            doubleArrayOf(20.64102151296647, -103.32983006801445),
            doubleArrayOf(20.6434714383289, -103.33143567864693),
            doubleArrayOf(20.645844755019517, -103.33292956340239),
            doubleArrayOf(20.64827445684351, -103.33444822095227),
            doubleArrayOf(20.650330679388855, -103.33576184930892),
            doubleArrayOf(20.65783679185825, -103.34386555281473),
            doubleArrayOf(20.65899096102669, -103.34523684371155),
            doubleArrayOf(20.662111200989653, -103.34817753276889),
            doubleArrayOf(20.663097942153552, -103.34948165722594),
            doubleArrayOf(20.665955577086333, -103.34851136797974),
            doubleArrayOf(20.669357361599637, -103.34736447870982),
            doubleArrayOf(20.670215878676608, -103.34665578518664)
        )
        val c47_panteon_kilometro13_refStops = arrayListOf(
            doubleArrayOf(20.66935591504175, -103.3462310817853),
            doubleArrayOf(20.66786326176203, -103.34609128366814),
            doubleArrayOf(20.66326346445079, -103.34583748144847),
            doubleArrayOf(20.65819894656298, -103.34566072416274),
            doubleArrayOf(20.65519975882808, -103.34493843610583),
            doubleArrayOf(20.65335806776933, -103.3437115330221),
            doubleArrayOf(20.648857950337998, -103.34085228595879),
            doubleArrayOf(20.646342968584896, -103.33923281329334),
            doubleArrayOf(20.641385560211393, -103.33593010676734),
            doubleArrayOf(20.636023218770678, -103.33132337987261),
            doubleArrayOf(20.633655453889034, -103.3292195373473),
            doubleArrayOf(20.631045201704715, -103.32689246337384),
            doubleArrayOf(20.628081710396927, -103.32489481524675),
            doubleArrayOf(20.627113570777947, -103.32173475732189),
            doubleArrayOf(20.621072928184102, -103.32167697963365),
            doubleArrayOf(20.6080671682928, -103.32820904600734),
            doubleArrayOf(20.597313782759134, -103.32482327100895),
            doubleArrayOf(20.595002926579355, -103.3262933148758),
            doubleArrayOf(20.594875710640597, -103.32901547361111),
            doubleArrayOf(20.594770116490803, -103.33120501717914),
            doubleArrayOf(20.59468244628328, -103.33357688292938),
            doubleArrayOf(20.59383093247588, -103.33376790919034),
            doubleArrayOf(20.59190464380783, -103.333458555938),
            doubleArrayOf(20.589501422924826, -103.33310185212096),
            doubleArrayOf(20.58509419999816, -103.33240122909355),
            doubleArrayOf(20.584143012635444, -103.33226714952667),
            doubleArrayOf(20.582395988077813, -103.33197262121719),
            doubleArrayOf(20.581332937634908, -103.33179026483066),
            doubleArrayOf(20.58010870919258, -103.33161810932685),
            doubleArrayOf(20.5782669877197, -103.33165841402817),
            doubleArrayOf(20.57752910651404, -103.3318669422327),
            doubleArrayOf(20.57625284310249, -103.32938294032259),
            doubleArrayOf(20.575752399360105, -103.32814894949045),
            doubleArrayOf(20.575189340697694, -103.32684077019398),
            doubleArrayOf(20.574794331004707, -103.32584813410038),
            doubleArrayOf(20.573541232522377, -103.32262930502932),
            doubleArrayOf(20.565938198768958, -103.32414119349335),
            doubleArrayOf(20.56328871953522, -103.31781542422353),
            doubleArrayOf(20.561337786797242, -103.30787415301018)
        )

    }
}