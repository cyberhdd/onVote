package com.example.onvote.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.datamodel.UserModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, 1){

    private val sp: SharedPreferences

    companion object {
        private const val TAG = "DatabaseHelper"
        const val DBNAME = "onVoteDev"
    }

    init {
        sp = context.getSharedPreferences("com.onvote.login", Context.MODE_PRIVATE)
    }

    override fun onCreate(myDB: SQLiteDatabase) {
        myDB.execSQL(
            "CREATE TABLE USERS(sID INTEGER PRIMARY KEY AUTOINCREMENT, sName TEXT, sFaculty TEXT, sUsername TEXT UNIQUE, sEmail TEXT, sPass TEXT, sVote INTEGER DEFAULT 0, isAdmin INTEGER DEFAULT 0)"
        )
        myDB.execSQL(
            "CREATE TABLE CANDIDATES(cID INTEGER PRIMARY KEY AUTOINCREMENT, sID INTEGER DEFAULT 0, cManif TEXT DEFAULT 'None', cAchieve DEFAULT 'None', cApprove INTEGER DEFAULT 0, cVotes INTEGER DEFAULT 0)"
        )
    }

    override fun onUpgrade(myDB: SQLiteDatabase, i: Int, i1: Int) {
        myDB.execSQL("DROP TABLE IF EXISTS USERS")
        myDB.execSQL("DROP TABLE IF EXISTS CANDIDATES")
        onCreate(myDB)
    }

    //insert user
    fun insertData(sName: String, sFaculty: String, sUsername: String, sEmail: String, sPass: String): Boolean {
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sName", sName)
        contentValues.put("sFaculty", sFaculty)
        contentValues.put("sUsername", sUsername)
        contentValues.put("sEmail", sEmail)
        contentValues.put("sPass", sPass)
        val result = myDB.insert("USERS", null, contentValues)

        return result != -1L
    }


    //insert candidate
    fun insertCandidate(sID:Int, cAchieve: String, cManif:String): Boolean{
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("sID", sID)
        contentValues.put("cAchieve", cAchieve)
        contentValues.put("cManif", cManif)
        val result = myDB.insert("CANDIDATES", null, contentValues)

        return result != -1L
    }

    //insert vote
    fun insertVote(cID: Int): Boolean{
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("cID", cID)
        val result = myDB.insert("CANDIDATES", null, contentValues)

        return result != -1L
    }

    //check username if exist
    fun checkUsername(sUsername: String): Boolean {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ?",
            arrayOf(sUsername)
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //check candidate if exist
    fun checkCandidate(sID: Int): Boolean {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM candidates WHERE sID = ?",
            arrayOf(sID.toString())
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //check user username & pass
    @SuppressLint("Range")
    fun checkUsernamePassword(sUsername: String, sPass: String): Int {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ? and sPass = ?",
            arrayOf(sUsername, sPass)
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val sID = cursor.getInt(0)
            cursor.close()
            Log.d(TAG, "checkUsernamePassword: $sID")
            return sID
        } else {
            cursor.close()
            return -1
        }
    }

    //check if user is admin
    @SuppressLint("Range")
    fun checkAdmin(sUsername: String, sPass: String): Int {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "SELECT * FROM users WHERE sUsername = ? and sPass = ? and isAdmin = 1",
            arrayOf(sUsername, sPass)
        )

        if (cursor.count > 0) {
            cursor.moveToFirst()
            val sID = cursor.getInt(0)
            cursor.close()
            Log.d(TAG, "adminID: $sID")
            return sID
        } else {
            cursor.close()
            return -1
        }
    }

    //update candidate vote
    fun candidateVote(cID:Int, cVotes:Int): Boolean {
        return try{
            val sqLiteDatabase = this.writableDatabase
            val userValues = ContentValues()
            userValues.put("cVotes", cVotes)
            sqLiteDatabase.update("CANDIDATES", userValues, "cID = ?", arrayOf(cID.toString()))
            true
        } catch (e: Exception){
            false
        }
    }

    //read user
    @SuppressLint("Range")
    fun getUser(sID: Int): UserModel? {
        val db = this.readableDatabase
        var userModel: UserModel? = null
        val query = "SELECT * FROM users WHERE sID = ?"
        val userCursor = db.rawQuery(query, arrayOf(sID.toString()))

        if (userCursor.moveToFirst()) {
            userModel = UserModel(
                userCursor.getInt(userCursor.getColumnIndex("sID")),
                userCursor.getString(userCursor.getColumnIndex("sName")),
                userCursor.getString(userCursor.getColumnIndex("sFaculty")),
                userCursor.getString(userCursor.getColumnIndex("sUsername")),
                userCursor.getString(userCursor.getColumnIndex("sEmail")),
                userCursor.getString(userCursor.getColumnIndex("sPass")),
                userCursor.getInt(userCursor.getColumnIndex("sVote")),
                userCursor.getInt(userCursor.getColumnIndex("isAdmin"))
            )
        }
        userCursor.close()
        return userModel
    }

    //read specific candidate
    @SuppressLint("Range")
    fun getCandidate(cID: Int): CandidateModel? {
        val db = this.readableDatabase
        var candidateModel: CandidateModel? = null
        val query = "SELECT * FROM candidates WHERE cID = ?"
        val userCursor = db.rawQuery(query, arrayOf(cID.toString()))

        if (userCursor.moveToFirst()) {
            candidateModel = CandidateModel(
                userCursor.getInt(userCursor.getColumnIndex("cID")),
                userCursor.getInt(userCursor.getColumnIndex("sID")),
                userCursor.getString(userCursor.getColumnIndex("cManif")),
                userCursor.getString(userCursor.getColumnIndex("cAchieve")),
                userCursor.getInt(userCursor.getColumnIndex("cApprove")),
                userCursor.getInt(userCursor.getColumnIndex("cVotes"))
            )
        }
        userCursor.close()
        return candidateModel
    }

    //read all approved candidate
    @SuppressLint("Range")
    fun getAllApprovedCandidate(): ArrayList<CandidateModel> {
        val db = readableDatabase
        val arrayList = ArrayList<CandidateModel>()
        val query = "SELECT * FROM candidates WHERE cApprove=?"
        val candidateCursor = db.rawQuery(query, arrayOf("1")) //1 for isApproved

        if (candidateCursor.count > 0) {
            while (candidateCursor.moveToNext()) {
                val candidateModel = CandidateModel(
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cID")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("sID")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cManif")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cAchieve")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cApprove")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cVotes"))
                )
                arrayList.add(candidateModel)
            }
        }
        candidateCursor.close()
        return arrayList
    }

    //read all approved faculty candidate
    @SuppressLint("Range")
    fun getAllApprovedFacultyCandidate(sFaculty: String): ArrayList<CandidateModel> {
        val db = readableDatabase
        val arrayList = ArrayList<CandidateModel>()
        //select StudentID from a faculty, then get candidates who are approved and in the faculty studentID list
        val query = "SELECT * FROM candidates WHERE cApprove=? AND sID IN (SELECT sID FROM users WHERE sFaculty=?)"
        val candidateCursor = db.rawQuery(query, arrayOf("1", sFaculty)) //1 for isApproved

        if (candidateCursor.count > 0) {
            while (candidateCursor.moveToNext()) {
                val candidateModel = CandidateModel(
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cID")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("sID")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cManif")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cAchieve")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cApprove")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cVotes"))
                )
                arrayList.add(candidateModel)
            }
        }
        candidateCursor.close()
        return arrayList
    }

    //read all pending candidate
    @SuppressLint("Range")
    fun getAllPendingCandidate(): ArrayList<CandidateModel> {
        val db = readableDatabase
        val arrayList = ArrayList<CandidateModel>()
        val query = "SELECT * FROM candidates WHERE cApprove=?"
        val candidateCursor = db.rawQuery(query, arrayOf("0")) //1 for isApproved

        if (candidateCursor.count > 0) {
            while (candidateCursor.moveToNext()) {
                val candidateModel = CandidateModel(
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cID")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("sID")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cManif")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cAchieve")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cApprove")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cVotes"))
                )
                arrayList.add(candidateModel)
            }
        }
        candidateCursor.close()
        return arrayList
    }

    //read all candidate
    @SuppressLint("Range")
    fun getAllCandidates(): ArrayList<CandidateModel> {
        val db = readableDatabase
        val arrayList = ArrayList<CandidateModel>()
        val query = "SELECT * FROM candidates"
        val candidateCursor = db.rawQuery(query, null)

        if (candidateCursor.count > 0) {
            while (candidateCursor.moveToNext()) {
                val candidateModel = CandidateModel(
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cID")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("sID")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cManif")),
                    candidateCursor.getString(candidateCursor.getColumnIndex("cAchieve")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cApprove")),
                    candidateCursor.getInt(candidateCursor.getColumnIndex("cVotes"))
                )
                arrayList.add(candidateModel)
            }
        }
        candidateCursor.close()
        return arrayList
    }


}