package com.example.onvote.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.onvote.CandidateInfoActivity
import com.example.onvote.DashboardActivity
import com.example.onvote.R
import com.example.onvote.datamodel.CandidateModel
import com.example.onvote.helper.DatabaseHelper
import com.example.onvote.helper.Session
import com.google.android.material.button.MaterialButton

class CandidateVoteAdapter(private val context: Context, private val candidateModelArrayList: ArrayList<CandidateModel>):
    RecyclerView.Adapter<CandidateVoteAdapter.ViewHolder>() {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private val session: Session = Session(context)

    //number of candidate model in array list
    override fun getItemCount(): Int {
        return candidateModelArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_vote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var model = candidateModelArrayList[position]
        var userModel = databaseHelper.getUser(model.sID)
        holder.tvCName.text = userModel?.sName
        holder.btnCVote.setOnClickListener{
            var votes = model.cVotes + 1
            if(databaseHelper.candidateVote(model.cID, votes)){
                Toast.makeText(context, "Vote success", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, "Vote error", Toast.LENGTH_SHORT).show()
            }
            Log.d("Candidate Votes: ", votes.toString())
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCName: TextView = itemView.findViewById(R.id.tvVoteCardCName)
        val btnCVote: MaterialButton = itemView.findViewById(R.id.btnVoteCardCVote)
    }

}