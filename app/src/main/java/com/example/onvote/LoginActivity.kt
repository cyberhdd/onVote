package com.example.onvote

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: MaterialButton
    private lateinit var dirRegister: MaterialButton
    private lateinit var DB: DatabaseHelper
    private lateinit var session: Session

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize
        username = findViewById(R.id.etLoginUsername)
        password = findViewById(R.id.etLoginPass)
        login = findViewById(R.id.btnLoginLogin)
        dirRegister = findViewById(R.id.btnLoginRegister)
        DB = DatabaseHelper(this)
        session = Session(this) // Shared helper instance

        login.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                val userID = DB.checkUsernamePassword(user, pass)
                if (userID > 0) {
                    session.setUserID(userID)
                    val isAdmin = DB.checkAdmin(user, pass)
                    var signInIntent = Intent(this, DashboardActivity::class.java)
                        //check if user is Admin
                        if (isAdmin > 0){
                            Toast.makeText(this, "Sign in successful as Admin", Toast.LENGTH_SHORT).show()
                            signInIntent = Intent(this, AdminHomeActivity::class.java)
                        }
                        else{
                            Toast.makeText(this, "Sign in successful as Student", Toast.LENGTH_SHORT).show()
                        }
                    startActivity(signInIntent)
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dirRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}