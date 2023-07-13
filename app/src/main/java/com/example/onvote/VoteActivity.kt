package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class VoteActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "VoteActivity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var faculty: TextView
    private lateinit var uName: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)


        btnLogout = findViewById(R.id.btnVoteLogout)
        faculty = findViewById(R.id.tvVoteFac)
        uName = findViewById(R.id.tvVoteName)
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
    }
}