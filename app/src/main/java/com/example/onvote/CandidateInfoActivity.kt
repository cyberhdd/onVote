package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class CandidateInfoActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "Candidate Info Activity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var uName: TextView
    private lateinit var cFacult: TextView
    private lateinit var cName: TextView
    private lateinit var cUsername: TextView
    private lateinit var cEmail: TextView
    private lateinit var cAchieve: TextView
    private lateinit var cManif: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_info)


        btnLogout = findViewById(R.id.btnInfoLogout)
        uName = findViewById(R.id.tvInfoName)
        cFacult = findViewById(R.id.tvInfoCFac)
        cName = findViewById(R.id.tvInfoCName)
        cUsername = findViewById(R.id.tvInfoCUsername)
        cEmail = findViewById(R.id.tvInfoCEmail)
        cAchieve = findViewById(R.id.tvInfoCAchieve)
        cManif = findViewById(R.id.tvInfoCManif)
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