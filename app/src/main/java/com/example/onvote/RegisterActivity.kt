package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.onvote.helper.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var pass: TextInputEditText
    private lateinit var repass: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var faculty: TextInputEditText
    private lateinit var register: MaterialButton
    private lateinit var dirLogin: MaterialButton
    private lateinit var DB: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize
        username = findViewById(R.id.etRegisterUsername)
        email = findViewById(R.id.etRegisterEmail)
        pass = findViewById(R.id.etRegisterPass)
        repass = findViewById(R.id.etRegisterRePass)
        name = findViewById(R.id.etRegisterSName)
        faculty = findViewById(R.id.etRegisterSFaculty)
        register = findViewById(R.id.btnRegisterRegister)
        dirLogin = findViewById(R.id.btnRegisterLogin)
        DB = DatabaseHelper(this)

        register.setOnClickListener {
            validate(
                username.text.toString(),
                email.text.toString(),
                name.text.toString(),
                faculty.text.toString(),
                pass.text.toString(),
                repass.text.toString()
            )
        }

        dirLogin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validate(user: String, email: String, name: String, faculty: String, pass: String, repass: String) {
        if (user.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        } else {
            if (pass == repass) {
                val checkuser = DB.checkUsername(user)
                if (!checkuser) {
                    val insert = DB.insertData(name, faculty, user, email, pass)
                    if (insert) {
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "User already exists, please sign in", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password not matching", Toast.LENGTH_SHORT).show()
            }
        }
    }
}