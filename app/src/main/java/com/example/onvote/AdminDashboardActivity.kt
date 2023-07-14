package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onvote.adapter.CandidateAdapter
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class AdminDashboardActivity : AppCompatActivity() {

    //tag for logs
    private val TAG = "Admin Dashboard Activity"

    private lateinit var btnLogout: MaterialButton
    private lateinit var uName: TextView
    private lateinit var rvDashboardAdmin: RecyclerView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var session: Session
    private lateinit var candidateArrayList : ArrayList<CandidateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        btnLogout = findViewById(R.id.btnAdminDashboardLogout)
        uName = findViewById(R.id.tvAdminDashboardName)
        rvDashboardAdmin = findViewById(R.id.rvDashboardAdmin)
        databaseHelper = DatabaseHelper(this)
        session = Session(this)


        val userID = session.getUserID()
        Log.d(TAG, "userID from Session: $userID")

        var userModel = databaseHelper.getUser(userID)

        uName.text = userModel?.sName

        candidateArrayList = databaseHelper.getAllApprovedCandidate()
        rvDashboardAdmin.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDashboardAdmin.adapter = CandidateAdapter(this, candidateArrayList)


        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }

    }
}