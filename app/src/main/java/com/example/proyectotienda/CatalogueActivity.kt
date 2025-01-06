package com.example.proyectotienda

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotienda.adapter.ClothAdapter
import com.example.proyectotienda.domain.ClothDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatalogueActivity : AppCompatActivity() {
    lateinit var adapter3: ClothAdapter
    lateinit var adapter4: ClothAdapter
    lateinit var adapter5: ClothAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothList: MutableList<ClothDomain>
    private lateinit var recyclerViewGamesList: RecyclerView
    private lateinit var recyclerViewSeriesList: RecyclerView
    private lateinit var recyclerViewAnimesList: RecyclerView
    private lateinit var allItems: List<ClothDomain>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)
        buttonNavigation()
        allItems = getAllItems()
        val savedSeriesList = loadClothList("Series")
        val savedAnimeList = loadClothList("Animes")
        val savedGamesList = loadClothList("Games")

        // Actualizar las listas 'allItems' con los datos guardados
        allItems = savedAnimeList + savedSeriesList + savedGamesList

        recyclerViewGames()
        recyclerViewSeries()
        recyclerViewAnimes()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email

        // Verificamos si el correo electrónico contiene "%@admin.com"
        val isAdmin = userEmail?.contains("@admin.com") == true

        // Obtenemos las referencias a los elementos que queremos ocultar/mostrar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)

        // Verificamos si el usuario es administrador y configuramos la visibilidad en consecuencia
        if (isAdmin) {
            // El usuario es administrador, mostramos el BottomNavigationView y el FloatingActionButton
            bottomNavigationView.visibility = View.VISIBLE
            floatingActionButton.visibility = View.VISIBLE
        } else {
            // El usuario no es administrador, ocultamos el BottomNavigationView y el FloatingActionButton
            bottomNavigationView.visibility = View.GONE
            floatingActionButton.visibility = View.GONE
        }


        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            showAddItemDialog(this)
            recyclerViewGames()
            recyclerViewSeries()
            recyclerViewAnimes()
        }

    }

    private fun showAddItemDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_item_dialog_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Agregar Nuevo Producto")
        builder.setView(dialogView)

        // Obtener referencias a los campos de edición y el Spinner en el cuadro de diálogo
        val editTitle = dialogView.findViewById<EditText>(R.id.newTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.newDescription)
        val editBrand = dialogView.findViewById<EditText>(R.id.newBrand)
        val editSize = dialogView.findViewById<EditText>(R.id.newSize)
        val editStar = dialogView.findViewById<EditText>(R.id.newStar)
        val editPrice = dialogView.findViewById<EditText>(R.id.newPrice)
        val editPicUrl = dialogView.findViewById<EditText>(R.id.newPicUrl)
        val categorySpinner = dialogView.findViewById<Spinner>(R.id.categorySpinner)
        val categories = arrayOf("Animes", "Series", "Games")

        // Creamos un ArrayAdapter usando el array de categorías y lo asignamos al Spinner
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Obtener los nuevos valores ingresados por el usuario
            val newCategory = categories[categorySpinner.selectedItemPosition] // Obtener la categoría seleccionada del Spinner
            val newTitle = editTitle.text.toString()
            val newDescription = editDescription.text.toString()
            val newBrand = editBrand.text.toString()
            val newSize = editSize.text.toString()
            val newStar = editStar.text.toString()
            val newPic = editPicUrl.text.toString()
            val newPrice = editPrice.text.toString().toDoubleOrNull()

            if (newPrice != null) {
                // Creamos un nuevo objeto ClothDomain con los nuevos valores
                val newItem = ClothDomain(newTitle, newPic, newDescription, newStar, newBrand, newSize, newPrice, newCategory)

                // Agregamos el nuevo elemento a la lista correspondiente según la categoría seleccionada
                when (newCategory) {
                    "Animes" -> {
                        val animeList = getClothListByCategory("Animes").toMutableList()
                        animeList.add(newItem)
                        saveClothList(animeList, "Animes")
                        adapter3.notifyDataSetChanged()
                    }
                    "Series" -> {
                        val seriesList = getClothListByCategory("Series").toMutableList()
                        seriesList.add(newItem)
                        saveClothList(seriesList, "Series")
                        adapter4.notifyDataSetChanged()
                    }
                    "Games" -> {
                        val gamesList = getClothListByCategory("Games").toMutableList()
                        gamesList.add(newItem)
                        saveClothList(gamesList, "Games")
                        adapter5.notifyDataSetChanged()
                    }
                }

                Toast.makeText(context, "Nuevo item agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun saveClothList(clothList: List<ClothDomain>, category: String) {
        val sharedPreferences = getSharedPreferences("cloth_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val clothListJson = Gson().toJson(clothList)
        editor.putString(category, clothListJson)

        editor.apply()
    }

    private fun loadClothList(category: String): List<ClothDomain> {
        val sharedPreferences = getSharedPreferences("cloth_prefs", Context.MODE_PRIVATE)
        val clothListJson = sharedPreferences.getString(category, null)

        return if (clothListJson != null) {
            val type = object : TypeToken<List<ClothDomain>>() {}.type
            Gson().fromJson(clothListJson, type)
        } else {
            emptyList()
        }
    }








    private fun buttonNavigation() {
        val homebtn: LinearLayout = findViewById(R.id.homeBtn)
        val cartbtn: LinearLayout = findViewById(R.id.cartBtn)
        val cataloguebtn: LinearLayout = findViewById(R.id.btnCatalogue)

        homebtn.setOnClickListener {
            startActivity(Intent(this@CatalogueActivity, MenuActivity::class.java))
            finish() // Finaliza la actividad actual después de iniciar la nueva actividad
        }

        cartbtn.setOnClickListener {
            startActivity(Intent(this@CatalogueActivity, CartActivity::class.java))
            finish() // Finaliza la actividad actual después de iniciar la nueva actividad
        }

        cataloguebtn.setOnClickListener {
            // Puedes mantener la misma actividad o cambiarla por una diferente según tus necesidades
            startActivity(Intent(this@CatalogueActivity, CatalogueActivity::class.java))
            finish() // Finaliza la actividad actual después de iniciar la nueva actividad
        }
    }

    private fun recyclerViewAnimes() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAnimesList = findViewById(R.id.view6)
        recyclerViewAnimesList.layoutManager = linearLayoutManager

        val clothList = getClothListByCategory("Animes") // Filtra los elementos por categoría
        adapter3 = ClothAdapter(ArrayList(clothList))
        recyclerViewAnimesList.adapter = adapter3
    }

    private fun recyclerViewSeries() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSeriesList = findViewById(R.id.view4)
        recyclerViewSeriesList.layoutManager = linearLayoutManager

        val clothList = getClothListByCategory("Series") // Filtra los elementos por categoría
        adapter4 = ClothAdapter(ArrayList(clothList))
        recyclerViewSeriesList.adapter = adapter4
    }

    private fun recyclerViewGames() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewGamesList = findViewById(R.id.view5)
        recyclerViewGamesList.layoutManager = linearLayoutManager

        val clothList = getClothListByCategory("Games") // Filtra los elementos por categoría
        adapter5 = ClothAdapter(ArrayList(clothList))
        recyclerViewGamesList.adapter = adapter5
    }


    private fun getClothListByCategory(category: String): List<ClothDomain> {
        return allItems.filter { it.category == category }
    }


    private fun getAllItems(): List<ClothDomain> {
        val clothList = mutableListOf<ClothDomain>()

        // Ejemplo de datos estáticos para Animes
        clothList.add(ClothDomain("Buzo Tokyo","camiseta_anime11","Disfraces de Cosplay de Anime japonés","4.4","Derick","S, M, XL, ",14.99, "Animes"))
        clothList.add(ClothDomain("Camiseta Ghibli","camiseta_anime12","Camisa de manga corta, color negro","4.6","Amazing","S",11.99, "Animes"))
        clothList.add(ClothDomain("Camiseta Hero","camiseta_anime13","Camiseta Blanca de Hero Academia","5","Derick","L, XL",12.99, "Animes"))
        clothList.add(ClothDomain("Buzo Academia","camiseta_anime14","Buzo negro sobre el Anime japonés","3.5","Derick","L, XL",17.99, "Animes"))
        clothList.add(ClothDomain("Abrigo Chainsaw","camiseta_anime15","Abrigo negro, cuello de camisa, estampadas abstractas","4.5","Black Clover","XL",20.99, "Animes"))

        // Ejemplo de datos estáticos para Series
        clothList.add(ClothDomain("Camiseta Magos","https://cdn.icon-icons.com/icons2/2107/PNG/512/file_type_angular_icon_130754.png","Disfraces de Serie de Television","4.4","Derick","S, M, XL, ",10.99, "Series"))
        clothList.add(ClothDomain("Camiseta Castle","camiseta_serie12","Camisa de manga corta, color blanco","4.6","Amazing","M, XL",15.99,"Series"))
        clothList.add(ClothDomain("Camiseta Park","camiseta_serie13","Serie de Television, Estampado ","5","South","L, XL",11.99,"Series"))
        clothList.add(ClothDomain("Camiseta Scream","camiseta_serie14","Serie de Terror, Manga corta","3","Screan","L, XL",9.99,"Series"))


        // Ejemplo de datos estáticos para VideoJuegos
        clothList.add(ClothDomain("Buzo Pacman","camiseta_videojuego11","Buzo blanco, Videojuego 1976, fantasmas, pacman","4.4","Derick","S, M, XL, ",11.99,"Games"))
        clothList.add(ClothDomain("Camiseta Cuphead","camiseta_videojuego12","Camisa de manga corta, color negro, juego popular","4.6","Amazing","M, XL",7.99,"Games"))
        clothList.add(ClothDomain("Camiseta Mortal","camiseta_videojuego13","Camisa de manga corta, color blanco, combate mortal","5","Derick","L, XL",12.99,"Games"))
        clothList.add(ClothDomain("Camiseta Luigi","camiseta_videojuego14","Manga corta color blanco, Mario Bros","4.5","Derick","L, XL",13.99,"Games"))
        clothList.add(ClothDomain("Camiseta L.O.L.","camiseta_videojuego15","Manga corta color negro, estampadas abstractas","4","Black Clover","XL",10.99,"Games"))


        return clothList
    }


}