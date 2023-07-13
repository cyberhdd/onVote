package com.example.onvote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
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

        //back on actionbar
        //not needed if child to parent
        //but if implemented back to previous activity without reset
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

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
        val cID = session.getCandidateID()
        Log.d(TAG, "cID from Session: $cID")

        var userModel = databaseHelper.getUser(userID)
        var candidateModel = databaseHelper.getCandidate(cID)
        var candidateUserModel = candidateModel?.let { databaseHelper.getUser(it.sID) }

        uName.text = userModel?.sName
        cFacult.text = candidateUserModel?.sFaculty
        cName.text = candidateUserModel?.sName
        cUsername.text = candidateUserModel?.sUsername
        cEmail.text = candidateUserModel?.sEmail
        cManif.text = candidateModel?.cManif
        cAchieve.text = candidateModel?.cAchieve



        btnLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            session.setUserID(0)
            startActivity(intent)
        }

    }

    //for the back button in action bar
    //not needed if child to parent
    //when implemented, back to parent activity without resting the parent
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //for the back button in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}