package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AdminHomeActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "Admin Candidate Application Activity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var btnResults: MaterialButton
    private lateinit var uName: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        btnLogout = findViewById(R.id.btnAdminAppLogout)
        btnResults = findViewById(R.id.btnAdminAppResult)
        uName = findViewById(R.id.tvAdminAppName)
        databaseHelper = DatabaseHelper(this)
        session = Session(this)

        val userID = session.getUserID()
        Log.d(TAG, "userID from Session: $userID")

        var userModel = databaseHelper.getUser(userID)

        uName.text = userModel?.sName


        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }

        btnResults.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }
    }


}