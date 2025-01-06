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

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtNombreR: EditText
    private lateinit var txtApellidoR: EditText
    private lateinit var txtEmailR: EditText
    private lateinit var txtPhoneR: EditText
    private lateinit var txtPasswordR: EditText
    private lateinit var btnRegisterR: Button
    private lateinit var originalHints: HashMap<EditText, String>
    private lateinit var mAuth: FirebaseAuth
    lateinit var email: String
    lateinit var password: String
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        txtNombreR = findViewById(R.id.txtNombreR)
        txtApellidoR = findViewById(R.id.txtApellidoR)
        txtEmailR = findViewById(R.id.txtEmailR)
        txtPhoneR = findViewById(R.id.txtPhoneR)
        txtPasswordR = findViewById(R.id.txtPasswordR)
        btnRegisterR = findViewById(R.id.btnRegisterR)


        btnRegisterR.setOnClickListener {
            validateFields()
        }

        originalHints = HashMap()
        originalHints[txtNombreR] = txtNombreR.hint.toString()
        originalHints[txtApellidoR] = txtApellidoR.hint.toString()
        originalHints[txtEmailR] = txtEmailR.hint.toString()
        originalHints[txtPhoneR] = txtPhoneR.hint.toString()
        originalHints[txtPasswordR] = txtPasswordR.hint.toString()

        btnRegisterR.setOnClickListener {
            if (true){
                email = txtEmailR.text.toString()
                password = txtPasswordR.text.toString()
                validateFields()




            }
        }

        txtNombreR.onFocusChangeListener = createFocusChangeListener(txtNombreR)
        txtApellidoR.onFocusChangeListener = createFocusChangeListener(txtApellidoR)
        txtEmailR.onFocusChangeListener = createFocusChangeListener(txtEmailR)
        txtPhoneR.onFocusChangeListener = createFocusChangeListener(txtPhoneR)
        txtPasswordR.onFocusChangeListener = createFocusChangeListener(txtPasswordR)
    }

    private fun validateAuthenticate() {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@RegisterActivity, IntroActivity::class.java)
                    startActivity(intent)
                    finish() // Opcional, si deseas cerrar la actividad actual
                    Toast.makeText(
                        baseContext,
                        "Se ha Registrado Correctamente",
                        Toast.LENGTH_SHORT,
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Hubo un error ingrese nuevamente",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }

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

        if (TextUtils.isEmpty(txtNombreR.text.toString().trim())) {
            txtNombreR.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtNombreR.hint = hintObligatorio
            txtNombreR.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return
        }

        if (TextUtils.isEmpty(txtApellidoR.text.toString().trim())) {
            txtApellidoR.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtApellidoR.hint = hintObligatorio
            txtApellidoR.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return
        }

        if (TextUtils.isEmpty(txtEmailR.text.toString().trim())) {
            txtEmailR.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtEmailR.hint = hintObligatorio
            txtEmailR.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return
        }

        if (email.contains("@admin.com")) {
            txtEmailR.setBackgroundResource(R.drawable.efecto_redondeo_error);
            txtEmailR.error = "No se permiten usuarios con '@admin.com'";
            return;
        }

        if (TextUtils.isEmpty(txtPhoneR.text.toString().trim())) {
            txtPhoneR.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtPhoneR.hint = hintObligatorio
            txtPhoneR.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return
        }

        if (TextUtils.isEmpty(txtPasswordR.text.toString().trim())) {
            txtPasswordR.setBackgroundResource(R.drawable.efecto_redondeo_error)
            txtPasswordR.hint = hintObligatorio
            txtPasswordR.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            return
        }

        validateAuthenticate()


    }

    private fun restoreOriginalHint(editText: EditText) {
        editText.setBackgroundResource(R.drawable.efecto_redondeo)
        editText.hint = originalHints[editText]
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.gris))
    }


}
