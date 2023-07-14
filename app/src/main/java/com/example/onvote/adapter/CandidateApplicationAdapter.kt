package com.example.onvote.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.onvote.AdminHomeActivity
import com.example.onvote.CandidateInfoActivity
import com.example.onvote.R
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class CandidateApplicationAdapter(private val context: Context, private val candidateModelArrayList: ArrayList<CandidateModel>):
    RecyclerView.Adapter<CandidateApplicationAdapter.ViewHolder>() {
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private val session: Session = Session(context)

    //number of candidate model in array list
    override fun getItemCount(): Int {
        return candidateModelArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_applicants, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model = candidateModelArrayList[position]
        var userModel = databaseHelper.getUser(model.sID)
        holder.tvCName.text = userModel?.sName

        holder.btnCApproval.setOnClickListener{
            if(databaseHelper.candidateApplicationStatus(model.cID)){
                Toast.makeText(context, "Candidate Approved", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Error: unable to approve candidate", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(context, AdminHomeActivity::class.java)
            context.startActivity(intent)
        }

        holder.btnCInfo.setOnClickListener{
            val intent = Intent(context, CandidateInfoActivity::class.java)
            session.setCandidateID(model.cID)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get views from card_meal_layout and assign to variables
        val tvCName: TextView = itemView.findViewById(R.id.tvAdminHomeCName)
        val btnCApproval: MaterialButton = itemView.findViewById(R.id.btnAdminHomeCApprove)
        val btnCInfo: MaterialButton = itemView.findViewById(R.id.btnAdminHomeCInfo)
    }
}