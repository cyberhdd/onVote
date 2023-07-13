package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CandidateApplicationActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "Candidate Application Activity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var btnSubmit: MaterialButton
    private lateinit var manifesto: TextInputEditText
    private lateinit var achievement: TextInputEditText
    private lateinit var faculty: TextView
    private lateinit var uName: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_application)

        btnLogout = findViewById(R.id.btnApplyLogout)
        btnSubmit = findViewById(R.id.btnApplySubmit)
        manifesto = findViewById(R.id.etApplyManifest)
        achievement = findViewById(R.id.etApplyAchieve)
        faculty = findViewById(R.id.tvApplyFac)
        uName = findViewById(R.id.tvApplyName)
        databaseHelper = DatabaseHelper(this)
        session = Session(this)

        val userID = session.getUserID()
        Log.d(TAG, "userID from Session: $userID")

        var userModel = databaseHelper.getUser(userID)

        uName.text = userModel?.sName
        faculty.text = userModel?.sFaculty


        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }

        btnSubmit.setOnClickListener {
            validate(
                achievement.text.toString(),
                manifesto.text.toString()
            )
        }
    }

    private fun validate(achievements: String, manifesto: String){
        if (achievements.isEmpty() || manifesto.isEmpty()) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "Your application has been submitted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}