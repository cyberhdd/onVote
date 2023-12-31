package com.example.onvote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onvote.adapter.CandidateAdapter
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class DashboardActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "HomeActivity"

    private lateinit var btnVote: MaterialButton
    private lateinit var btnApply: MaterialButton
    private lateinit var btnLogout: MaterialButton
    /*private lateinit var btnInfo: MaterialButton*/
    private lateinit var faculty: TextView
    private lateinit var uName: TextView
    private lateinit var rvDashboardUser: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    private lateinit var candidateArrayList : ArrayList<CandidateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnVote = findViewById(R.id.btnDashboardVote)
        btnApply = findViewById(R.id.btnDashboardApply)
        btnLogout = findViewById(R.id.btnDashboardLogout)
        /*btnInfo = findViewById(R.id.btnDashboardInfo)*/
        faculty = findViewById(R.id.tvDashboardFac)
        uName = findViewById(R.id.tvDashboardName)
        rvDashboardUser = findViewById(R.id.rvDashboardUser)
        databaseHelper = DatabaseHelper(this)
        session = Session(this)

        val userID = session.getUserID()
        Log.d(TAG, "userID from Session: $userID")

        var userModel = databaseHelper.getUser(userID)

        uName.text = userModel?.sName
        faculty.text = userModel?.sFaculty


        candidateArrayList = databaseHelper.getAllApprovedFacultyCandidate(userModel!!.sFaculty)
        rvDashboardUser.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDashboardUser.adapter = CandidateAdapter(this, candidateArrayList)



        btnVote.setOnClickListener {
            val intent = Intent(this, VoteActivity::class.java)
            startActivity(intent)
        }

        btnApply.setOnClickListener {
            val intent = Intent(this, CandidateApplicationActivity::class.java)
            startActivity(intent)
        }


        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }

    }
}