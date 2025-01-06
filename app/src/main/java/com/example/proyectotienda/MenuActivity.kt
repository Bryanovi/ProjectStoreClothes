package com.example.proyectotienda

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotienda.adapter.CategoryAdapter
import com.example.proyectotienda.adapter.ClothAdapter
import com.example.proyectotienda.baseDatos.AppBaseDatos
import com.example.proyectotienda.domain.CategoryDomain
import com.example.proyectotienda.domain.ClothDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MenuActivity : AppCompatActivity() {
    lateinit var adapter: RecyclerView.Adapter<*>
    lateinit var adapter2: RecyclerView.Adapter<*>
    private lateinit var recyclerViewCategoryList: RecyclerView
    private lateinit var recyclerViewPopularList: RecyclerView
    private lateinit var appDatabase: AppBaseDatos
    private lateinit var auth:FirebaseAuth
    private lateinit var button:Button
    private lateinit var user: FirebaseUser
    private val clothList: MutableList<ClothDomain> = mutableListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        recyclerViewCategory()
        recyclerViewPopular()
        buttonNavigation()
        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.btnLogout)
        user = auth.currentUser!!
        if (user == null){
            startActivity(Intent(this@MenuActivity, IntroActivity::class.java))
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email

        // Verificamos si el correo electrónico contiene "%@admin.com"
        val isAdmin = userEmail?.contains("@admin.com") == true


        button.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MenuActivity, IntroActivity::class.java))
        })
        /*val categorySpinner: Spinner = findViewById(R.id.categorySpinner)
        val categories = arrayOf("Popular", "Series", "Animes", "VideoJuegos")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

         */

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
        }
    }

    private fun showAddItemDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_item_dialog_layout, null)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Agregar nuevo item")
        builder.setView(dialogView)

        // Obtenemos las referencias a los campos de edición en el cuadro de diálogo
        val editTitle = dialogView.findViewById<EditText>(R.id.newTitle)
        val editDescription = dialogView.findViewById<EditText>(R.id.newDescription)
        val editBrand = dialogView.findViewById<EditText>(R.id.newBrand)
        val editSize = dialogView.findViewById<EditText>(R.id.newSize)
        val editStar = dialogView.findViewById<EditText>(R.id.newStar)
        val editPrice = dialogView.findViewById<EditText>(R.id.newPrice)
        val editPicUrl = dialogView.findViewById<EditText>(R.id.newPicUrl)

        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Obtenemos los nuevos valores ingresados por el usuario
            val newTitle = editTitle.text.toString()
            val newDescription = editDescription.text.toString()
            val newBrand = editBrand.text.toString()
            val newSize = editSize.text.toString()
            val newStar = editStar.text.toString()
            val newPic = editPicUrl.text.toString()
            val newPrice = editPrice.text.toString().toDoubleOrNull()

            if (newPrice != null) {
                // Creamos un nuevo objeto ClothDomain con los nuevos valores
                val newItem = ClothDomain(newTitle, newPic, newDescription, newStar, newBrand, newSize, newPrice)

                // Agregamos el nuevo elemento a la lista
                clothList.add(newItem)
                adapter.notifyDataSetChanged()

                Toast.makeText(context, "Nuevo item agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Ingrese un precio válido", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            // No hacemos ninguna acción
            dialog.dismiss()
        }

        builder.show()
    }


    private fun buttonNavigation() {
        val homebtn: LinearLayout = findViewById(R.id.homeBtn)
        val cartbtn: LinearLayout = findViewById(R.id.cartBtn)
        val cataloguebtn: LinearLayout = findViewById(R.id.btnCatalogue)

        homebtn.setOnClickListener {
            // Use FLAG_ACTIVITY_REORDER_TO_FRONT to bring an existing instance of CatalogueActivity to the front
            val intent = Intent(this@MenuActivity, MenuActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        cartbtn.setOnClickListener {
            // Use FLAG_ACTIVITY_REORDER_TO_FRONT to bring an existing instance of CatalogueActivity to the front
            val intent = Intent(this@MenuActivity, CartActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        cataloguebtn.setOnClickListener {
            // Use FLAG_ACTIVITY_REORDER_TO_FRONT to bring an existing instance of CatalogueActivity to the front
            val intent = Intent(this@MenuActivity, CatalogueActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }






    }

    private fun recyclerViewPopular() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopularList = findViewById(R.id.view2)
        recyclerViewPopularList.layoutManager = linearLayoutManager


        val clothList = ArrayList<ClothDomain>()
        clothList.add(ClothDomain("Camiseta Anime","camiseta_anime1","Disfraces de Cosplay de Anime japonés","4.4","Derick","S, M, XL, ",11.99))
        clothList.add(ClothDomain("Camiseta Básica","camiseta_basico1","Camisa de manga corta, color blanco","4.6","Amazing","M, XL",7.99))
        clothList.add(ClothDomain("Camiseta Serie","camiseta_serie1","Disfraces de Cosplay de Anime japonés","5","Derick","L, XL",12.99))
        clothList.add(ClothDomain("Camiseta Yoshi","camiseta_videojuego1","Disfraces de Cosplay de Anime japonés","3","Derick","L, XL",13.99))
        clothList.add(ClothDomain("Camiseta Stone","camiseta_musica1","Camisa de manga corta, cuello de camisa, estampadas abstractas","4","Black Clover","XL",10.99))

        val adapter2 = ClothAdapter(clothList)
        recyclerViewPopularList.setAdapter(adapter2)


    }




    private fun recyclerViewCategory() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategoryList = findViewById(R.id.view1)
        recyclerViewCategoryList.layoutManager = linearLayoutManager

        val categoryList = ArrayList<CategoryDomain>()
        categoryList.add(CategoryDomain("Anime", "ropa_anime"))
        categoryList.add(CategoryDomain("Música", "ropa_musica"))
        categoryList.add(CategoryDomain("VideoJuegos", "ropa_videojuegos"))
        categoryList.add(CategoryDomain("Series", "ropa_series"))

        val adapter = CategoryAdapter(categoryList)
        recyclerViewCategoryList.setAdapter(adapter)


    }







}