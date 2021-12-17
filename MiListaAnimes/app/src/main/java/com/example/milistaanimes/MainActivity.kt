package com.example.milistaanimes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.milistaanimes.Controlador.ItemsNav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.milistaanimes.ui.theme.MiListaAnimesTheme
import coil.compose.rememberImagePainter
import com.example.milistaanimes.Modelos.ContactosJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiListaAnimesTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PantallaPrincipal()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun PantallaPrincipal() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState =scaffoldState,
        topBar = {TopBar(scope = scope, scaffoldState = scaffoldState)},
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        Navigation(navController = navController)
    }
}



@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {

    TopAppBar(
        title = { Text(text = "Navigation Drawer", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = Color.LightGray,
        contentColor = Color.Black
    )

}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
    val items = listOf(
        ItemsNav.Home,
        ItemsNav.Insertar,
        ItemsNav.Favoritos
    )
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
            Image(
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = "Imagen de fondos",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(15.dp))
    }
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { items ->
            DrawerItem(item = items, selected = currentRoute == items.ruta, onItemClick = {

                navController.navigate(items.ruta) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }

            })
        }
    }

@Composable
fun DrawerItem(item: ItemsNav, selected: Boolean, onItemClick: (ItemsNav) -> Unit) {
    val background = if (selected) Color.Blue.copy(alpha = 0.25f) else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12))
            .background(background)
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = item.icono,
            contentDescription = item.titulo,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text= item.titulo,
            style = TextStyle(fontSize = 18.sp),
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen() {
    val contactosJson = cargarJson()

    if(!contactosJson.isEmpty()){

        Log.d("CONTACTO", contactosJson.toString())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(174, 198, 207)),
            verticalArrangement = Arrangement.Center,
        ) {
            val context = LocalContext.current
            val deletedItem = remember { mutableStateListOf<ContactosJson>() }
            textoInicio(titulo = "CONTACTOS ")
            LazyColumn(modifier = Modifier .fillMaxSize()){
                itemsIndexed(
                    items = contactosJson,
                    itemContent = {_,contactos->
                        AnimatedVisibility(visible = !deletedItem.contains(contactos), enter = expandVertically(),
                            exit = shrinkHorizontally(animationSpec = tween(durationMillis = 700))
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth()
                                            .background(Color(156, 52, 52)),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        cargarImagen(url = "https://cdn.pixabay.com/photo/2020/07/01/12/58/icon-5359553_960_720.png")
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                        ) {
                                            cargarTexto(texto = contactos.Nombre)
                                            cargarEspacio(texto = "")
                                            cargarTextoDescripcion(texto = contactos.Tlf)
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                        ) {
                                            IconButton(onClick={
                                                borrar(contactos, context)
                                                deletedItem.add(contactos)
                                            }){
                                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Borrar")
                                            }
                                            IconButton(onClick={
                                                insertarContactoFavorito(contactos.Nombre, contactos.Tlf)
                                                Toast.makeText(context,"Contacto añiadido a favoritos", Toast.LENGTH_SHORT).show()
                                            }){
                                                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favoritos")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun InsertarScreen() {
    var NombreContacto by rememberSaveable { mutableStateOf("") }
    var TelefonoContacto by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(174, 198, 207)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Datos para el nuevo Contacto",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )

        val url = "https://emser.es/wp-content/uploads/2016/08/usuario-sin-foto.png"
        Image(
            painter = rememberImagePainter(url), contentDescription = "Imagen",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .clip(shape = CircleShape)
                .border(
                    border = BorderStroke(3.dp, Color.White),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentScale = ContentScale.None,
        )

        TextField(
            value = NombreContacto,
            onValueChange = { nuevo ->
                NombreContacto = nuevo
            },
            label = {
                Text(
                    text = "Nombre del Contacto:",
                )

            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )

        TextField(
            value = TelefonoContacto,
            onValueChange = { nuevo ->
                TelefonoContacto = nuevo
            },
            label = {
                Text(
                    text = "Telefono del Contacto:",
                )

            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )
        Button(
            colors = ButtonDefaults.textButtonColors(backgroundColor = Color(156, 52, 52)),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 300.dp, height = 50.dp),
            onClick = {
                if (TelefonoContacto.isEmpty() || NombreContacto.isEmpty()){
                    Toast.makeText(context, "Error, no se pueden insertar contactos con los campos vacios", Toast.LENGTH_SHORT).show()
                }else{
                    insertarContacto(NombreContacto, TelefonoContacto)
                    TelefonoContacto = ""
                    NombreContacto = ""
                    Toast.makeText(context, "Contacto insertado correctamente", Toast.LENGTH_SHORT).show()
                }
        }) {
            Text ("INSERTAR",
                    color = Color.Black,
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = ItemsNav.Home.ruta) {

        composable(ItemsNav.Home.ruta) {
            HomeScreen()
        }

        composable(ItemsNav.Insertar.ruta) {
            InsertarScreen()
        }
        composable(ItemsNav.Favoritos.ruta){
            FavoritosScreen()
        }
    }

}

@Composable
fun cargarImagen(url: String) {
    Image(
        painter = rememberImagePainter(url), contentDescription = "Imagen",
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(shape = CircleShape)
            .border(
                border = BorderStroke(3.dp, Color.White),
                shape = androidx.compose.foundation.shape.CircleShape
            ),
        contentScale = ContentScale.FillHeight,
    )
}


@Composable
fun cargarTexto(texto: String) {
    Text(
        text = "Nombre: "+texto,
        textAlign = TextAlign.Center,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .background(Color(156, 52, 52))
    )
}

@Composable
fun cargarEspacio(texto: String) {
    Text(
        text=texto
    )
}

@Composable
fun cargarTextoDescripcion(texto: String) {
    Text(
        text = "Tlf: " +texto,
        textAlign = TextAlign.Center,
        fontSize = 22.sp,
        color = Color.White,
        modifier = Modifier
            .background(Color(156, 52, 52))


    )
}

fun borrar(contacto: ContactosJson, context: Context){
    val url = "http://iesayala.ddns.net/AlejandroMoles/borrarContacto.php/?Tlf=${contacto.Tlf}"
    insertarUrl(url)
    Toast.makeText(context, "Borrado el contacto " + contacto.Nombre, Toast.LENGTH_SHORT).show()
}

fun borrarFav(contacto: ContactosJson, context: Context){
    val url = "http://iesayala.ddns.net/AlejandroMoles/borrarContactoFavorito.php/?Tlf=${contacto.Tlf}"
    insertarUrl(url)
    Toast.makeText(context, "Borrado el contacto " + contacto.Nombre + " de la lista de Favoritos", Toast.LENGTH_SHORT).show()
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun FavoritosScreen() {
    val contactosJson : ContactoInfo = cargarJsonFav()

    if(!contactosJson.isEmpty()){

        Log.d("CONTACTO", contactosJson.toString())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(174, 198, 207)),
            verticalArrangement = Arrangement.Center,
        ) {
            val context = LocalContext.current
            val deletedItem = remember { mutableStateListOf<ContactosJson>() }
            textoInicio(titulo = "CONTACTOS FAVORITOS")

            LazyColumn(modifier = Modifier .fillMaxSize()){
                itemsIndexed(
                    items = contactosJson,
                    itemContent = {_,contactos->
                        AnimatedVisibility(visible = !deletedItem.contains(contactos), enter = expandVertically(),
                            exit = shrinkHorizontally(animationSpec = tween(durationMillis = 700))
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth()
                                            .background(Color(156, 52, 52)),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        cargarImagen(url = "https://cdn.pixabay.com/photo/2020/07/01/12/58/icon-5359553_960_720.png")
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                        ) {
                                            cargarTexto(texto = contactos.Nombre)
                                            cargarEspacio(texto = "")
                                            cargarTextoDescripcion(texto = contactos.Tlf)
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                        ) {
                                            IconButton(onClick={
                                                borrarFav(contactos, context)
                                                deletedItem.add(contactos)
                                            }){
                                                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Borrar")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }


}

@Composable
fun textoInicio(titulo: String) {
    Text(
        text = titulo,
        fontSize = 35.sp,
        fontFamily = FontFamily.SansSerif,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier
            .padding(7.dp)
            .fillMaxWidth()
    )
}


fun insertarContacto(nombre:String, tlf:String){
    var url = "http://iesayala.ddns.net/AlejandroMoles/insertarContactos.php/?Nombre=$nombre&Tlf=$tlf"
    insertarUrl(url)
}

fun insertarContactoFavorito(nombre:String, tlf:String){
    var url = "http://iesayala.ddns.net/AlejandroMoles/insertarContactosFavoritos.php/?Nombre=$nombre&Tlf=$tlf"
    insertarUrl(url)
}



fun insertarUrl(urlString:String){
    GlobalScope.launch(Dispatchers.IO)   {
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        } catch (e: Exception) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        }
    }
}


@Composable
fun cargarJson(): ContactoInfo {

    var users by rememberSaveable { mutableStateOf(ContactoInfo()) }
    val user = ContactoInstance.contactoInterface.contactoinformation()

    user.enqueue(object : Callback<ContactoInfo> {
        override fun onResponse(
            call: Call<ContactoInfo>,
            response: Response<ContactoInfo>
        ) {
            val userInfo: ContactoInfo? = response.body()
            if (userInfo != null) {
                users = userInfo
            }
        }

        override fun onFailure(call: Call<ContactoInfo>, t: Throwable)
        {
            //Error
        }

    })
    return users
}

@Composable
fun cargarJsonFav(): ContactoInfo {

    var users by rememberSaveable { mutableStateOf(ContactoInfo()) }
    val user = ContFavInstance.contactoFavInterface.contactofavinformation()

    user.enqueue(object : Callback<ContactoInfo> {
        override fun onResponse(
            call: Call<ContactoInfo>,
            response: Response<ContactoInfo>
        ) {
            val userInfo: ContactoInfo? = response.body()
            if (userInfo != null) {
                users = userInfo
            }
        }

        override fun onFailure(call: Call<ContactoInfo>, t: Throwable)
        {
            //Error
        }

    })
    return users
}