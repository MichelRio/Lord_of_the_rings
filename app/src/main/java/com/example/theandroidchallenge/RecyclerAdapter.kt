package com.example.theandroidchallenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    companion object {
        var titles: Array<Array<String>> = Array(0){ Array(3) {""}}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        Picasso.get().load(getCover(titles[position][0])).into(holder.itemImage)
        holder.itemTitle.text = titles[position][1]
        holder.itemDesc.text = "Award Nominations : "+ titles[position][2]
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var itemImage : ImageView
        var itemTitle : TextView
        var itemDesc  : TextView

        init {
            itemImage   = itemView.findViewById(R.id.ivAvatar)
            itemTitle   = itemView.findViewById(R.id.tvName)
            itemDesc    = itemView.findViewById(R.id.tvGender)

            itemView.setOnClickListener(){
                val position = adapterPosition
                val title = titles[position][0]
                listener.onItemClick(position, title)
                Toast.makeText(itemView.context, titles[position][1], Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClick(p0: View?) {
            this@RecyclerAdapter
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, title: String)
    }

    fun getCover(idMovie: String) : Int{
        when (idMovie) {
            "5cd95395de30eff6ebccde56" -> return R.drawable.poster_lotr_series
            "5cd95395de30eff6ebccde57" -> return R.drawable.poster_hobbit_series
            "5cd95395de30eff6ebccde58" -> return R.drawable.poster_unexpected_journey
            "5cd95395de30eff6ebccde59" -> return R.drawable.poster_desolation_of_smaug
            "5cd95395de30eff6ebccde5a" -> return R.drawable.poster_five_armies
            "5cd95395de30eff6ebccde5b" -> return R.drawable.poster_two_towers
            "5cd95395de30eff6ebccde5c" -> return R.drawable.poster_fellowship_of_the_ring
            "5cd95395de30eff6ebccde5d" -> return R.drawable.poster_return_king
            else -> return R.drawable.bkg_lotr
        }
    }
}