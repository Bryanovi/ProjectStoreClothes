package com.example.proyectotienda

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var txtEmailL: EditText
    private lateinit var txtPasswordL: EditText
    private lateinit var btnLoginL: Button
    private lateinit var originalHints: HashMap<EditText, String>
    lateinit var email: String
    lateinit var password: String
    private lateinit var mAuth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        txtEmailL = findViewById(R.id.txtEmailL)
        txtPasswordL = findViewById(R.id.txtPasswordL)
        btnLoginL = findViewById(R.id.btnLoginL)

        originalHints = HashMap()
        originalHints[txtEmailL] = txtEmailL.hint.toString()
        originalHints[txtPasswordL] = txtPasswordL.hint.toString()

        btnLoginL.setOnClickListener {
            email = txtEmailL.text.toString()
            password = txtPasswordL.text.toString()
            validateFields()


        }

        txtEmailL.onFocusChangeListener = createFocusChangeListener(txtEmailL)
        txtPasswordL.onFocusChangeListener = createFocusChangeListener(txtPasswordL)

    }



    private fun createFocusChangeListener(editText: EditText): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.setBackgroundResource(R.drawable.efecto_redondeo)
                editText.hint = originalHints[editText]
                editText.setHintTextColor(ContextCompat.getColor(this, R.color.gris))
            }
        }
    }

    private fun validateFields() {
        val hintObligatorio = getString(R.string.hint_obligatorio)

        if (TextUtils.isEmpty(txtEmailL.text.toString().trim())) {
            txtEmailL.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtEmailL.hint = hintObligatorio
            txtEmailL.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return;
        }

        if (TextUtils.isEmpty(txtPasswordL.text.toString().trim())) {
            txtPasswordL.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtPasswordL.hint = hintObligatorio
            txtPasswordL.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return;
        }

        validateAuthenticate()


    }

    private fun validateAuthenticate() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = mAuth.currentUser

                    // Verificamos si el correo electrónico contiene "%@admin.com"
                    if (currentUser?.email?.contains("@admin.com") == true) {
                        // El usuario es administrador, redirige a la actividad de administrador
                        Toast.makeText(
                            baseContext,
                            "Inicio de sesión exitoso como administrador.",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Aquí debes dirigir al usuario a la actividad para administradores
                        // Por ejemplo:
                         startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                    } else {
                        // El usuario es un usuario normal, redirige a la actividad de usuario normal
                        Toast.makeText(
                            baseContext,
                            "Inicio de sesión exitoso como usuario normal.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                    }

                    finish() // Termina la actividad actual para que el usuario no pueda volver atrás presionando el botón "Atrás"
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario
                    Toast.makeText(
                        baseContext,
                        "Usuario o Contraseña Incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }





}