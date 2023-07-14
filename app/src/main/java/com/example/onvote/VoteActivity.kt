package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onvote.adapter.CandidateAdapter
import com.example.onvote.adapter.CandidateVoteAdapter
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class VoteActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "VoteActivity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var faculty: TextView
    private lateinit var uName: TextView
    private lateinit var rvVoteUser: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    private lateinit var candidateArrayList : ArrayList<CandidateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)


        btnLogout = findViewById(R.id.btnVoteLogout)
        faculty = findViewById(R.id.tvVoteFac)
        uName = findViewById(R.id.tvVoteName)
        rvVoteUser = findViewById(R.id.rvVoteUser)
        databaseHelper = DatabaseHelper(this)
        session = Session(this)

        val userID = session.getUserID()
        Log.d(TAG, "userID from Session: $userID")

        var userModel = databaseHelper.getUser(userID)

        uName.text = userModel?.sName
        faculty.text = userModel?.sFaculty

        candidateArrayList = databaseHelper.getAllApprovedFacultyCandidate(userModel!!.sFaculty)
        rvVoteUser.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvVoteUser.adapter = CandidateVoteAdapter(this, candidateArrayList)


        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }
    }
}