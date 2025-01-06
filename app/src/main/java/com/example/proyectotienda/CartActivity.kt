package com.example.proyectotienda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotienda.adapter.CartListAdapter
import com.example.proyectotienda.helper.ManagmentCart


class CartActivity : AppCompatActivity(){
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var recyclerViewList: RecyclerView
    private lateinit var managmentCart: ManagmentCart
    private lateinit var txtTotalPrice: TextView
    private lateinit var txtIVA: TextView
    private lateinit var txtLocal: TextView
    private lateinit var txtTotal: TextView
    private lateinit var txtEmpty: TextView
    private var IVA: Double = 0.0
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        managmentCart = ManagmentCart(this)
        initView()
        initList()
        buttonNavigation()
        calculateCard()

    }

    private fun buttonNavigation() {
        val homebtn: LinearLayout = findViewById(R.id.homeBtn)
        val cartbtn: LinearLayout = findViewById(R.id.cartBtn)
        val cataloguebtn : LinearLayout = findViewById(R.id.btnCatalogue)

        homebtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, MenuActivity::class.java))
        }

        cartbtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, CartActivity::class.java))
        }
        cataloguebtn.setOnClickListener {
            startActivity(Intent(this@CartActivity, CatalogueActivity::class.java))
        }
    }

    private fun initList() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewList.layoutManager = linearLayoutManager
        adapter = CartListAdapter(managmentCart.listCart, this
        ) { calculateCard() }
        recyclerViewList.adapter = adapter

        if (managmentCart.listCart.isEmpty()) {
            txtEmpty.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
        } else {
            txtEmpty.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }
    }

    private fun calculateCard() {
        var porcentIVA: Double = 0.12;
        var local: Double = 10.0;

        val price = Math.round((managmentCart.getTotalPrice() * porcentIVA) * 100.0) / 100.0
        val total = Math.round((managmentCart.getTotalPrice() + IVA + local ) * 100.0) / 100.0
        val itemTotal = Math.round(managmentCart.getTotalPrice() * 100.0)/100.0

        txtTotalPrice.setText("$" + itemTotal)
        txtIVA.setText("$" + price)
        txtLocal.setText("$" + local)
        txtTotal.setText("$" + total)

    }

    private fun initView() {
        txtTotalPrice = findViewById(R.id.txtTotalPrice)
        txtIVA = findViewById(R.id.txtIVA)
        txtLocal = findViewById(R.id.txtLocal)
        txtTotal = findViewById(R.id.txtTotal)
        recyclerViewList = findViewById(R.id.view)
        scrollView = findViewById(R.id.scrollView)
        txtEmpty = findViewById(R.id.txtEmpty)
    }

}