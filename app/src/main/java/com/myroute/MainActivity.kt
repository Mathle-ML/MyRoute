package com.myroute

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.caverock.androidsvg.BuildConfig
import com.myroute.dbmanager.DBManager
import com.myroute.mapmanager.MapManager
import kotlinx.coroutines.Deferred
import java.io.File


class MainActivity : AppCompatActivity() {
    var coroutineManager: Deferred<Unit>? = null

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.othermain)

        val c47_refPoints = arrayListOf(
            doubleArrayOf(20.55906907492559, -103.30774074699116),
            doubleArrayOf(20.559415548186234, -103.30861938354877),
            doubleArrayOf(20.56002172775039, -103.31066946096331),
            doubleArrayOf(20.56125535594053, -103.31318642945362),
            doubleArrayOf(20.561442591981695, -103.31395127913397),
            doubleArrayOf(20.56144591467465, -103.3159624450161),
            doubleArrayOf(20.561591463140505, -103.31629024378088),
            doubleArrayOf(20.562874972617365, -103.31729072483594),
            doubleArrayOf(20.563421183297343, -103.31810343446601),
            doubleArrayOf(20.567172935321896, -103.32710007312751),
            doubleArrayOf(20.567645190844633, -103.32723899026482),
            doubleArrayOf(20.569453790918928, -103.32745473329419),
            doubleArrayOf(20.570071644946925, -103.32706479654776),
            doubleArrayOf(20.57467458878119, -103.32580906094582),
            doubleArrayOf(20.575185551631527, -103.32671381844996),
            doubleArrayOf(20.577319349309622, -103.33178078635301),
            doubleArrayOf(20.57796961131237, -103.33167701534006),
            doubleArrayOf(20.58439937735429, -103.33214423496368),
            doubleArrayOf(20.584602015161877, -103.33196068911911),
            doubleArrayOf(20.590711530754348, -103.32135668866434),
            doubleArrayOf(20.590760492575274, -103.32053861467686),
            doubleArrayOf(20.590165215455446, -103.32121553322835),
            doubleArrayOf(20.619234997984382, -103.32161266987583),
            doubleArrayOf(20.627464649546248, -103.32116520381493),
            doubleArrayOf(20.65234571419694, -103.33728731905849),
            doubleArrayOf(20.662060132067307, -103.34820508406695),
            doubleArrayOf(20.662621082765703, -103.34958205599172),
            doubleArrayOf(20.66308317793317, -103.34959538199458),
            doubleArrayOf(20.669849249393582, -103.34731304059977),
            doubleArrayOf(20.670234601940884, -103.34668711498605)
        )

        val c47_refStops = arrayListOf(
            doubleArrayOf(20.559069100394936, -103.30774048935166),
            doubleArrayOf(20.561456570070064, -103.31604658469399),
            doubleArrayOf(20.563235785264652, -103.31763104974158),
            doubleArrayOf(20.56469949238684, -103.32127527092894),
            doubleArrayOf(20.567190860973902, -103.32709031302566),
            doubleArrayOf(20.570063401051026, -103.32703112202485),
            doubleArrayOf(20.57283265476147, -103.32628294491856),
            doubleArrayOf(20.57467267498566, -103.32576950150975),
            doubleArrayOf(20.57572408157145, -103.32800674986521),
            doubleArrayOf(20.577945464610053, -103.33168301365389),
            doubleArrayOf(20.58115194769593, -103.3316402825391),
            doubleArrayOf(20.58433858802349, -103.33212226072389),
            doubleArrayOf(20.587463552093585, -103.3267864373036),
            doubleArrayOf(20.593203092278024, -103.32268804302421),
            doubleArrayOf(20.596920431976415, -103.32438407012077),
            doubleArrayOf(20.60595056687493, -103.3156904788965),
            doubleArrayOf(20.620381875577547, -103.32141232353541),
            doubleArrayOf(20.627431563062196, -103.32116875269962),
            doubleArrayOf(20.631638125327704, -103.32381991513408),
            doubleArrayOf(20.641059372411203, -103.32986839649176),
            doubleArrayOf(20.643508086787406, -103.33145863811818),
            doubleArrayOf(20.645833824510404, -103.33292900341527),
            doubleArrayOf(20.650283682204037, -103.33572714503629),
            doubleArrayOf(20.653131575970853, -103.33824727559421),
            doubleArrayOf(20.65891993993047, -103.34519453345408),
            doubleArrayOf(20.662070735592852, -103.34820960500036),
            doubleArrayOf(20.66313677525082, -103.34950504902298),
            doubleArrayOf(20.670214057767016, -103.34667228834493)
        )

        val c87_refPoints = arrayListOf(
            doubleArrayOf(20.684291722687245, -103.37078600549557),
            doubleArrayOf(20.60388923837909, -103.29624610549799),
            doubleArrayOf(20.604718970321517, -103.29611440549799),
            doubleArrayOf(20.60606019799264, -103.29626660552024),
            doubleArrayOf(20.607029327018317, -103.29623575952716),
            doubleArrayOf(20.60799114500764, -103.29623939865672),
            doubleArrayOf(20.608981441459154, -103.29623791163714),
            doubleArrayOf(20.60933668519389, -103.29732152407541),
            doubleArrayOf(20.61002457530655, -103.2990481961369),
            doubleArrayOf(20.610986260690115, -103.30045830228978),
            doubleArrayOf(20.61270853096634, -103.30265245253025),
            doubleArrayOf(20.613454774650883, -103.30164997693008),
            doubleArrayOf(20.61345351941051, -103.30028540307639),
            doubleArrayOf(20.613940540230136, -103.30023058791198),
            doubleArrayOf(20.615420552662048, -103.2999850061402),
            doubleArrayOf(20.61617117633156, -103.29989582268978),
            doubleArrayOf(20.616972419279993, -103.29976812144643),
            doubleArrayOf(20.616934762860293, -103.29731255909194),
            doubleArrayOf(20.61689898925397, -103.29581320424802),
            doubleArrayOf(20.618280386794655, -103.29695878941016),
            doubleArrayOf(20.619311532610105, -103.29533605295543),
            doubleArrayOf(20.62078189115544, -103.292959698338),
            doubleArrayOf(20.621952883595124, -103.29362936849385),
            doubleArrayOf(20.62286760371984, -103.2941167776252),
            doubleArrayOf(20.62232286103224, -103.29588435334512),
            doubleArrayOf(20.621927482054538, -103.29617202026435),
            doubleArrayOf(20.621367673871806, -103.29636312765919),
            doubleArrayOf(20.621438591391197, -103.29710542899754),
            doubleArrayOf(20.621129817532108, -103.29787790519224),
            doubleArrayOf(20.620188429949337, -103.2980388377399),
            doubleArrayOf(20.619379459571718, -103.29820178193958),
            doubleArrayOf(20.619379459571718, -103.29820178193958),
            doubleArrayOf(20.620186440052894, -103.30104437148951),
            doubleArrayOf(20.620469484535942, -103.3025262919808),
            doubleArrayOf(20.620780770099852, -103.3037534026202),
            doubleArrayOf(20.62164245040699, -103.30390561797275),
            doubleArrayOf(20.624892966630163, -103.30415509607913),
            doubleArrayOf(20.62591587328614, -103.30442099370683),
            doubleArrayOf(20.62552991721679, -103.30704620574929),
            doubleArrayOf(20.62658297878859, -103.30707034564139),
            doubleArrayOf(20.627313465375952, -103.3070596168077),
            doubleArrayOf(20.628460648577125, -103.30705157017535),
            doubleArrayOf(20.62924258578779, -103.30703815913076),
            doubleArrayOf(20.629855079536018, -103.30702608918861),
            doubleArrayOf(20.631205569347756, -103.30703413582408),
            doubleArrayOf(20.631900891209177, -103.30709850884244),
            doubleArrayOf(20.632519649692224, -103.30708777999877),
            doubleArrayOf(20.63332290238239, -103.30708375669198),
            doubleArrayOf(20.634825224557066, -103.30708912111587),
            doubleArrayOf(20.636355884020176, -103.30720354915013),
            doubleArrayOf(20.637441501859836, -103.30942844152581),
            doubleArrayOf(20.637478923485872, -103.31069609702185),
            doubleArrayOf(20.637514064734027, -103.31147796095507),
            doubleArrayOf(20.637555032472857, -103.3125797988872),
            doubleArrayOf(20.637598959013413, -103.31363860089955),
            doubleArrayOf(20.63763472775559, -103.31494416613965),
            doubleArrayOf(20.638352169413302, -103.31657737959688),
            doubleArrayOf(20.639384337957885, -103.3164655003977),
            doubleArrayOf(20.641488474821784, -103.3169914555785),
            doubleArrayOf(20.643497652967877, -103.31847053540541),
            doubleArrayOf(20.64715302911341, -103.32126405190459),
            doubleArrayOf(20.64970492802867, -103.32303828138092),
            doubleArrayOf(20.653013265462203, -103.32554834575308),
            doubleArrayOf(20.65374864477263, -103.32607674092161),
            doubleArrayOf(20.65611820825128, -103.32784093569458),
            doubleArrayOf(20.65731160481306, -103.32871667694725),
            doubleArrayOf(20.659664322223172, -103.33044839582216),
            doubleArrayOf(20.661637620965372, -103.33196538061719),
            doubleArrayOf(20.6631786256029, -103.33309271209998),
            doubleArrayOf(20.664345295094655, -103.3328121794899)
        )

        val linea1_refPoints = arrayListOf(
            doubleArrayOf(20.607208044860997, -103.40107474337024),
            doubleArrayOf(20.61692505554818, -103.39308990301627),
            doubleArrayOf(20.621165265866292, -103.38966365952334),
            doubleArrayOf(20.627666714425402, -103.38434476232585),
            doubleArrayOf(20.636251372092715, -103.37813464068407),
            doubleArrayOf(20.63853046789745, -103.37672003258824),
            doubleArrayOf(20.639530853009155, -103.37595646453417),
            doubleArrayOf(20.646628416181972, -103.3697747870088),
            doubleArrayOf(20.64962024437278, -103.3674144501557),
            doubleArrayOf(20.654688078151626, -103.3634176439981),
            doubleArrayOf(20.656038843737477, -103.36229231814866),
            doubleArrayOf(20.659048829355342, -103.35967794090834),
            doubleArrayOf(20.660050506192963, -103.35793833162334),
            doubleArrayOf(20.6644531588347, -103.35606513206233),
            doubleArrayOf(20.66690477986916, -103.35538902410468),
            doubleArrayOf(20.668932135809566, -103.35483806454243),
            doubleArrayOf(20.66968420086183, -103.354812546693),
            doubleArrayOf(20.677239558058616, -103.3547087672635),
            doubleArrayOf(20.67873191435122, -103.35421277274942),
            doubleArrayOf(20.68989713482705, -103.3538453461288),
            doubleArrayOf(20.693251416562926, -103.3540376391584),
            doubleArrayOf(20.69625300465201, -103.35503129312009),
            doubleArrayOf(20.69941827576931, -103.35486574765824),
            doubleArrayOf(20.703569160300923, -103.3547257125203),
            doubleArrayOf(20.70385527341848, -103.3548021817322),
            doubleArrayOf(20.70632102912255, -103.35483232497289),
            doubleArrayOf(20.70910834845898, -103.35616824299376),
            doubleArrayOf(20.711127466052872, -103.35580362590035),
            doubleArrayOf(20.714204182786535, -103.35454587542142),
            doubleArrayOf(20.71552374912302, -103.35453633055675),
            doubleArrayOf(20.722177232506297, -103.3530888358851),
            doubleArrayOf(20.727940329259116, -103.35320720026655),
            doubleArrayOf(20.729063454113785, -103.3530098568864),
            doubleArrayOf(20.730640432371565, -103.35231007977504),
            doubleArrayOf(20.732656491262336, -103.35156341774936),
            doubleArrayOf(20.734515654398116, -103.35118876385988),
            doubleArrayOf(20.735973047492063, -103.35054158965593)
        )

        val linea1_refStops = arrayListOf(
            doubleArrayOf(20.60716514596298, -103.40110078640475),
            doubleArrayOf(20.613802916488797, -103.39567698542493),
            doubleArrayOf(20.62143135386513, -103.38941638869473),
            doubleArrayOf(20.62680909443844, -103.38507779487927),
            doubleArrayOf(20.63284590177309, -103.38053127682161),
            doubleArrayOf(20.638191953213106, -103.3769941045913),
            doubleArrayOf(20.64311774624295, -103.37275467281245),
            doubleArrayOf(20.6473922112713, -103.36913498336389),
            doubleArrayOf(20.65427743511517, -103.3636814848014),
            doubleArrayOf(20.661080133954385, -103.3574642340312),
            doubleArrayOf(20.66680020006465, -103.35542327158161),
            doubleArrayOf(20.674571992395357, -103.35476877945406),
            doubleArrayOf(20.68212700300859, -103.35377681011877),
            doubleArrayOf(20.691765417381873, -103.35403127794993),
            doubleArrayOf(20.698215788179276, -103.35478649222547),
            doubleArrayOf(20.70799546193674, -103.3555786981932),
            doubleArrayOf(20.720829108295288, -103.3534372133916),
            doubleArrayOf(20.730634681312246, -103.35237065809594),
            doubleArrayOf(20.73596306442153, -103.35052277637165)
        )

        val linea2_refPoints = arrayListOf(
            doubleArrayOf(20.659905065755137, -103.27604058919407),
            doubleArrayOf(20.660050073927017, -103.27766771591331),
            doubleArrayOf(20.660729751678627, -103.27884447339089),
            doubleArrayOf(20.661567552675585, -103.28023574123162),
            doubleArrayOf(20.661793799629706, -103.28143396372722),
            doubleArrayOf(20.66423304532663, -103.29944200486791),
            doubleArrayOf(20.66471635322926, -103.30390669518495),
            doubleArrayOf(20.67105387667101, -103.3251810702911),
            doubleArrayOf(20.675407980677484, -103.34120997220872),
            doubleArrayOf(20.674938406369172, -103.35536709107528)
        )

        val linea2_refStops = arrayListOf(
            doubleArrayOf(20.66006488694061, -103.27604157036892),
            doubleArrayOf(20.66243337432479, -103.28585448030192),
            doubleArrayOf(20.663944173827566, -103.29750625022524),
            doubleArrayOf(20.665316960665013, -103.30618211469105),
            doubleArrayOf(20.66752549008662, -103.31346182825285),
            doubleArrayOf(20.670324412110503, -103.32286944049686),
            doubleArrayOf(20.672783879474256, -103.33149301616109),
            doubleArrayOf(20.67550382155234, -103.34088775428073),
            doubleArrayOf(20.67516975385934, -103.34813242376491),
            doubleArrayOf(20.674954216618787, -103.35540300637277)
        )

        val linea3_refPoints = arrayListOf(
            doubleArrayOf(20.62305511853962, -103.28508023166734),
            doubleArrayOf(20.6252205686272, -103.29124006524543),
            doubleArrayOf(20.636594970016816, -103.29939329485836),
            doubleArrayOf(20.648394444457658, -103.30607365165918),
            doubleArrayOf(20.665129923530692, -103.33284636696088),
            doubleArrayOf(20.66718644983381, -103.33539129347001),
            doubleArrayOf(20.671055803037635, -103.34490592872118),
            doubleArrayOf(20.671730560947022, -103.34727912781865),
            doubleArrayOf(20.672891871308835, -103.34726202694591),
            doubleArrayOf(20.695339161201225, -103.34859149349079),
            doubleArrayOf(20.719608235207858, -103.38650064360638),
            doubleArrayOf(20.72235354489169, -103.38771550731481),
            doubleArrayOf(20.72794161869268, -103.38639793617024),
            doubleArrayOf(20.728944485622208, -103.39053429297776),
            doubleArrayOf(20.74122761535859, -103.40736649051591)
        )

        val linea3_refStops = arrayListOf(
            doubleArrayOf(20.741147219969427, -103.40734787853341),
            doubleArrayOf(20.738178156255035, -103.40329729896054),
            doubleArrayOf(20.72910724954168, -103.38958226140468),
            doubleArrayOf(20.720715116447405, -103.387076794545),
            doubleArrayOf(20.712342834026693, -103.37505094962044),
            doubleArrayOf(20.70651150502515, -103.3660203970919),
            doubleArrayOf(20.69938489219515, -103.35479922509741),
            doubleArrayOf(20.692719994428252, -103.34837547710939),
            doubleArrayOf(20.684115373691824, -103.34799590272954),
            doubleArrayOf(20.671041903554876, -103.3449821390897),
            doubleArrayOf(20.66513524907675, -103.3327242003685),
            doubleArrayOf(20.65956493763132, -103.32412393678564),
            doubleArrayOf(20.651051095546514, -103.31045007792581),
            doubleArrayOf(20.64473819071061, -103.30408721230559),
            doubleArrayOf(20.637483704537825, -103.29992756991896),
            doubleArrayOf(20.632018622807085, -103.29616073825285),
            doubleArrayOf(20.623158829873926, -103.28516807366576)
        )

        var dbmanager = DBManager(this)
        dbmanager.addRoute("C47 Kilometro 13 | Dos templos", c47_refPoints, c47_refStops, R.color.green, "bus")
        dbmanager.addRoute("C47 Panteon | Dos templos", c47_refPoints, c47_refStops, R.color.green, "bus")
        dbmanager.addRoute("C87 | Manuel M o Degollado", c87_refPoints, c87_refPoints, R.color.green, "bus")
        dbmanager.addRoute("T04 Loma Dorada | Juan Gil Preciado", c87_refPoints, c87_refPoints,R.color.green, "bus")
        dbmanager.addRoute("T04 Loma Dorada | Pedro Alarcon", c87_refPoints, c87_refPoints, R.color.green, "bus")
        dbmanager.addRoute("T18 Lopez Mateos | San Elías", c87_refPoints, c87_refPoints, R.color.red, "bus")
        dbmanager.addRoute("T18 Lopez Mateos | Washington", c87_refPoints, c87_refPoints, R.color.red, "bus")

        dbmanager.addRoute("Linea 1 | Juárez - Aviación", linea1_refPoints, linea1_refStops, R.color.brown, "train")
        dbmanager.addRoute("Linea 2 | Tetlán - Tonalá", linea2_refPoints, linea2_refStops, R.color.green_train, "train")
        dbmanager.addRoute("Linea 3 | Central nueva - Zapopan", linea3_refPoints, linea3_refStops, R.color.pink, "train")

        checkPermissionsAndCopyDatabase()

        val shareButton: Button = findViewById(R.id.buton)
        shareButton.setOnClickListener {
            val filePath = filesDir.absolutePath + "/basededatos/db.sqlite3"
            shareFile(filePath)
        }

    }
    private val PERMISSION_REQUEST_CODE = 123
    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = ArrayList<String>()

        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
            }
        } else {
            // Todos los permisos requeridos ya han sido otorgados
            copyDatabaseToDocumentsDirectory()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Se otorgaron todos los permisos solicitados
                    copyDatabaseToDocumentsDirectory()
                } else {
                    // Al menos un permiso no fue otorgado
                    // Aquí puedes manejar el caso en que el usuario deniegue los permisos
                }
            }
        }
    }

    // Llama a esta función para verificar y solicitar los permisos en tiempo real
    private fun checkPermissionsAndCopyDatabase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (result == PackageManager.PERMISSION_GRANTED) {
                // Permiso ya otorgado
                copyDatabaseToDocumentsDirectory()
            } else {
                // Solicitar permisos en tiempo real
                checkAndRequestPermissions()
            }
        } else {
            // Versión de Android anterior a 6.0, no se requieren permisos en tiempo real
            copyDatabaseToDocumentsDirectory()
        }
    }

    fun copyDatabaseToDocumentsDirectory(){
        var dbManager = DBManager(this)
        if(dbManager.copyDatabaseToDocumentsDirectory()){
            Log.i("MyRoute:Info", "El path es")
        }else{
            Log.e("MyRoute:Error", "error falso")
        }
    }

    private fun shareFile(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            // El archivo no existe, mostrar un mensaje de error o realizar alguna acción adecuada
            return
        }

        // Crear el intent para compartir el archivo
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*" // Define el tipo de archivo a compartir

        // Uri del archivo a compartir
        val fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file)

        // Asignar el archivo URI al intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

        // Agregar flags para otorgar permisos de lectura a las aplicaciones receptoras
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Iniciar la actividad para compartir
        startActivity(Intent.createChooser(shareIntent, "Compartir archivo"))
    }
}