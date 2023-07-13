package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class AdminDashboardActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "Admin Dashboard Activity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var uName: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        btnLogout = findViewById(R.id.btnAdminDashboardLogout)
        uName = findViewById(R.id.tvAdminDashboardName)
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

    }
}